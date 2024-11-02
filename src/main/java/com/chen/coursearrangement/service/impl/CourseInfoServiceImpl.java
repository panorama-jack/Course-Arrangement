package com.chen.coursearrangement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.coursearrangement.entity.CourseInfo;
import com.chen.coursearrangement.entity.Dict;
import com.chen.coursearrangement.entity.Teacher;
import com.chen.coursearrangement.entity.dto.CourseInfoDTO;
import com.chen.coursearrangement.entity.dto.CoursePlanDTO;
import com.chen.coursearrangement.mapper.CourseInfoMapper;
import com.chen.coursearrangement.mapper.DictMapper;
import com.chen.coursearrangement.mapper.TeacherMapper;
import com.chen.coursearrangement.service.ICourseInfoService;
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
 * @since 2024-01-17
 */
@Service
public class CourseInfoServiceImpl extends ServiceImpl<CourseInfoMapper, CourseInfo> implements ICourseInfoService {

    @Resource
    CourseInfoMapper courseInfoMapper;

    @Resource
    DictMapper dictMapper;

    @Resource
    TeacherMapper teacherMapper;


    @Override
    public List<CoursePlanDTO> findCourse() {
        return courseInfoMapper.findCourse();
    }

    @Override
    public int lastThreeDigits(String college, String courseAttribute) {
        Integer result = courseInfoMapper.lastThreeDigits(college, courseAttribute);
        return result != null ? result : 0;
    }

    @Override
    public List<CourseInfo> export(String college, String courseAttribute, String courseName) {
        return courseInfoMapper.export(college, courseAttribute, courseName);
    }
    //课程信息导入
    @Override
    public int imp(List<CourseInfoDTO> courseInfoDTOS) {
        List<Map<String, Object>> collegeList = dictMapper.selectMaps(new QueryWrapper<Dict>().eq("type", "college").select("name", "value"));
        List<Map<String, Object>> courseAttributeList = dictMapper.selectMaps(new QueryWrapper<Dict>().eq("type", "courseAttribute").select("name", "value"));
        List<Map<String, Object>> teacherList = teacherMapper.selectMaps(new QueryWrapper<Teacher>().select("teacher_name AS name", "teacher_no AS value"));
        List<Map<String, Object>> classroomList = dictMapper.selectMaps(new QueryWrapper<Dict>().eq("type", "classroom").select("name", "value"));

        List<CourseInfo> courseInfoList = new ArrayList<>();
        for (CourseInfoDTO studentDTO : courseInfoDTOS) {
            CourseInfo courseInfo = new CourseInfo();
            courseInfo.setCollegeNo(findValueByName(studentDTO.getCollege(), collegeList));
            courseInfo.setCourseAttribute(findValueByName(studentDTO.getCourseAttribute(), courseAttributeList));
            courseInfo.setCourseNo(studentDTO.getCourseNo());
            courseInfo.setCourseName(studentDTO.getCourseName());
            courseInfo.setTeacherNo(findValueByName(studentDTO.getTeacherName(), teacherList));
            courseInfo.setClassroomType(findValueByName(studentDTO.getClassroomType(), classroomList));
            courseInfoList.add(courseInfo);
        }
        return courseInfoMapper.imp(courseInfoList);
    }
    //将汉字转化为对应的编码
    private String findValueByName(String name, List<Map<String, Object>> dictList) {
        // 使用Stream API进行查找
        return dictList.stream()
                .filter(dict -> name.equals(dict.get("name")))
                .map(dict -> (String) dict.get("value"))
                .findFirst()
                .orElse(null);  // 如果找不到对应的值，可以根据需要返回一个默认值或者抛出异常
    }
}
