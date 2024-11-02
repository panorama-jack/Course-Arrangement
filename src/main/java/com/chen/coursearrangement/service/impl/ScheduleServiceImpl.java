package com.chen.coursearrangement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.coursearrangement.entity.Schedule;
import com.chen.coursearrangement.entity.dto.ScheduleDTO;
import com.chen.coursearrangement.mapper.ScheduleMapper;
import com.chen.coursearrangement.service.IScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author CHQ
 */
@Service
@Slf4j
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule> implements IScheduleService{

    @Resource
    private  ScheduleMapper scheduleMapper;

    @Override
    public List<ScheduleDTO> queryScheduleByClassNo(String term, String classNo) {
        return scheduleMapper.queryScheduleByClassNo(term,classNo);
    }


    @Override
    public List<ScheduleDTO> queryScheduleByTeacherNo(String term, String teacherNo) {
        return scheduleMapper.queryScheduleByTeacherNo(term,teacherNo);
    }

    @Override
    public List<Map<String, Object>> selectDistinctClassesByTermAndTeacher(String term, String teacherNo) {
        return scheduleMapper.selectDistinctClassesByTermAndTeacher(term,teacherNo);
    }

    @Override
    public List<String> getCourseTimeList(String classNo, String teacherNo) {
        return scheduleMapper.getCourseTimeList(classNo,teacherNo);
    }

    @Override
    public List<Map<String, String>> getClassroomList(String classroomNo,String courseTime) {
        return scheduleMapper.getClassroomList(classroomNo,courseTime);
    }
}
