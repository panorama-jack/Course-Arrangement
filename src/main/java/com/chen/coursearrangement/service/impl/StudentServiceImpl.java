package com.chen.coursearrangement.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.coursearrangement.entity.ClassInfo;
import com.chen.coursearrangement.entity.Dict;
import com.chen.coursearrangement.entity.Student;
import com.chen.coursearrangement.entity.User;
import com.chen.coursearrangement.entity.dto.StudentDTO;
import com.chen.coursearrangement.mapper.ClassInfoMapper;
import com.chen.coursearrangement.mapper.DictMapper;
import com.chen.coursearrangement.mapper.StudentMapper;
import com.chen.coursearrangement.mapper.UserMapper;
import com.chen.coursearrangement.service.IStudentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Jack Chen
 * @since 2023-05-12
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

    @Resource
    private StudentMapper studentMapper;
    @Resource
    private UserMapper userMapper;

    @Resource
    private DictMapper dictMapper;
    @Resource
    private ClassInfoMapper classInfoMapper;


    @Override
    public Page<Student> findPage(Page<Student> page, String college, String grade, String classNo, String studentNo, String studentName) {
        return studentMapper.findPage(page, college, grade, classNo, studentNo, studentName);
    }

    @Override
    public int getMaxLastTwoDigits(String classNo) {
        Integer result= studentMapper.getMaxLastTwoDigits(classNo);
        return result != null ? result : 0;
    }

    @Override
    public List<StudentDTO> export(String college, String grade, String classNo, String studentNo, String studentName) {
        return studentMapper.export(college, grade, classNo, studentNo, studentName);
    }

    @Override
    public int imp(List<StudentDTO> studentDTOS) {
        // 获取字典数据，缓存在方法外部
        List<Map<String, Object>> gradeList = dictMapper.selectMaps(new QueryWrapper<Dict>().eq("type", "grade").select("name", "value"));
        List<Map<String, Object>> collegeList = dictMapper.selectMaps(new QueryWrapper<Dict>().eq("type", "college").select("name", "value"));
        List<Map<String, Object>> classList = classInfoMapper.selectMaps(new QueryWrapper<ClassInfo>().select("class_name AS name", "class_no AS value"));

        // 创建一个用于保存 Student 对象的列表
        List<Student> studentList = new ArrayList<>();

        for (StudentDTO studentDTO : studentDTOS) {
            Student student = new Student();
            student.setGradeNo(findValueByName(studentDTO.getGrade(), gradeList));
            student.setCollegeNo(findValueByName(studentDTO.getCollege(), collegeList));
            student.setClassNo(findValueByName(studentDTO.getClassName(), classList));
            student.setStudentNo(studentDTO.getStudentNo());
            student.setStudentName(studentDTO.getStudentName());
            student.setTelephone(studentDTO.getTelephone());
            studentList.add(student);
            addStudentUser(student);
            //修改班级人数
            studentMapper.updateClassStudentNumber(student.getClassNo(),1);
        }
        return studentMapper.imp(studentList);
    }

    @Override
    public int addStudentUser(Student student) {
        User user=new User();
        user.setUserNo(student.getStudentNo());
        user.setUsername(student.getStudentName());
        //截取学号后六位作为密码
        String ciphertext = SaSecureUtil.aesEncrypt(user.getUserNo()+student.getStudentNo().substring(student.getStudentNo().length() - 6),student.getStudentNo().substring(student.getStudentNo().length() - 6));
        user.setPassword(ciphertext);
        user.setRole("ROLE_STUDENT");
        user.setAvatarUrl("http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg");
        user.setSignature("努力学习！");
        //先删后改   防止系统中存在两个一样的用户  导致登录出错
        userMapper.removeByUserNo(user.getUserNo());
        return userMapper.insert(user);
    }

    @Override
    public int deleteStudentUser(Integer id) {
        Student student = studentMapper.selectById(id);
        if (student != null) {
            String studentNo = student.getStudentNo();
            if (studentNo != null) {
                return userMapper.removeByUserNo(studentNo);
            } else {
                // 处理学生号码为null的情况
                return 0; // 或者抛出异常，记录警告等
            }
        } else {
            // 处理找不到给定id的学生的情况
            return 0; // 或者抛出异常，记录警告等
        }
    }

    @Override
    public Page<StudentDTO> findPageByTeacher(Page<StudentDTO> page, String term, String userNo, String classNo, String studentNo, String studentName) {
        return studentMapper.findPageByTeacher(page,term,userNo,classNo,studentNo,studentName);
    }

    @Override
    public List<StudentDTO> exportByTeacher(String term,String userNo, String classNo, String studentNo, String studentName) {
        return studentMapper.exportByTeacher(term,userNo,classNo,studentNo,studentName);
    }

    @Override
    public int updateClassStudentNumber(String classNo, Integer num) {
        return studentMapper.updateClassStudentNumber(classNo,num);
    }

    private String findValueByName(String name, List<Map<String, Object>> dictList) {
        // 使用Stream API进行查找
        return dictList.stream()
                .filter(dict -> name.equals(dict.get("name")))
                .map(dict -> (String) dict.get("value"))
                .findFirst()
                .orElse(null);  // 如果找不到对应的值，可以根据需要返回一个默认值或者抛出异常
    }

}
