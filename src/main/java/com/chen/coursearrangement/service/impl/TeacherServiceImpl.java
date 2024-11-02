package com.chen.coursearrangement.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.coursearrangement.entity.Dict;
import com.chen.coursearrangement.entity.Teacher;
import com.chen.coursearrangement.entity.User;
import com.chen.coursearrangement.entity.dto.TeacherDTO;
import com.chen.coursearrangement.mapper.DictMapper;
import com.chen.coursearrangement.mapper.TeacherMapper;
import com.chen.coursearrangement.mapper.UserMapper;
import com.chen.coursearrangement.service.ITeacherService;
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
 * @since 2024-01-15
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements ITeacherService {

    @Resource
    private TeacherMapper teacherMapper;

    @Resource
    private DictMapper dictMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public Page<Teacher> findPage(Page<Teacher> page, String teacherNo, String teacherName, String college) {
        return teacherMapper.findPage(page, teacherNo, teacherName, college);
    }

    @Override
    public int getMaxLastThreeDigits(String college) {

        Integer result = teacherMapper.getMaxLastThreeDigits(college);
        return result != null ? result : 0;
    }


    @Override
    public List<TeacherDTO> export(String college, String teacherNo, String teacherName) {
        return teacherMapper.export(college, teacherNo, teacherName);
    }

    @Override
    public int imp(List<TeacherDTO> list) {

        List<Map<String, Object>> collegeList = dictMapper.selectMaps(new QueryWrapper<Dict>().eq("type", "college").select("name", "value"));
        List<Map<String, Object>> professionList = dictMapper.selectMaps(new QueryWrapper<Dict>().eq("type", "profession").select("name", "value"));

        // 创建一个用于保存 Student 对象的列表
        List<Teacher> teacherList = new ArrayList<>();
        for (TeacherDTO teacherDTO : list) {
            Teacher teacher = new Teacher();
            teacher.setCollegeNo(findValueByName(teacherDTO.getCollege(), collegeList));
            teacher.setProfession(findValueByName(teacherDTO.getProfession(), professionList));
            teacher.setTeacherNo(teacherDTO.getTeacherNo());
            teacher.setTeacherName(teacherDTO.getTeacherName());
            teacher.setTelephone(teacherDTO.getTelephone());
            teacherList.add(teacher);
            addTeacherUser(teacher);
        }
        return teacherMapper.imp(teacherList);
    }

    @Override
    public int addTeacherUser(Teacher teacher) {
        User user = new User();
        user.setUserNo(teacher.getTeacherNo());
        user.setUsername(teacher.getTeacherName());
        //9+工号作为密码
        String ciphertext = SaSecureUtil.aesEncrypt(user.getUserNo()+"9"+ teacher.getTeacherNo(), "9"+teacher.getTeacherNo());
        user.setPassword(ciphertext);
        user.setRole("ROLE_TEACHER");
        user.setAvatarUrl("http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg");
        user.setSignature("好好工作！");
        //先删后改
        userMapper.removeByUserNo(user.getUserNo());
        return  userMapper.insert(user);
    }
    @Override
    public int deleteTeacherUser(Integer id) {
        return userMapper.removeByUserNo(teacherMapper.selectById(id).getTeacherNo());
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