package com.chen.coursearrangement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.coursearrangement.entity.Schedule;
import com.chen.coursearrangement.entity.dto.ScheduleDTO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jack Chen
 * @since 2024-01-12
 */
public interface IScheduleService extends IService<Schedule> {

    List<ScheduleDTO> queryScheduleByClassNo(String term, String classNo);

    List<ScheduleDTO> queryScheduleByTeacherNo(String term, String teacherNo);

    List<Map<String, Object>> selectDistinctClassesByTermAndTeacher(String term, String teacherNo);

    List<String> getCourseTimeList(String classNo, String teacherNo);

    List<Map<String, String>> getClassroomList(String classroomNo,String courseTime);
}
