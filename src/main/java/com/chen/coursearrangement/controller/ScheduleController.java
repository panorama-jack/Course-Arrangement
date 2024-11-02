package com.chen.coursearrangement.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.chen.coursearrangement.common.Result;
import com.chen.coursearrangement.entity.Schedule;
import com.chen.coursearrangement.entity.dto.ScheduleDTO;
import com.chen.coursearrangement.service.IScheduleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @author Jack Chen
 * @since 2024-01-12
 */

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Resource
    private IScheduleService scheduleService;

    /**
     *  根据学期，班级查询课程表
     * @param term
     * @param classNo
     * @return
     */
    @GetMapping
    public Result queryScheduleByClassNo(@RequestParam("term") String term,@RequestParam("classNo") String classNo) {
        List<ScheduleDTO> scheduleList = scheduleService.queryScheduleByClassNo(term,classNo);

        return Result.success(scheduleList);
    }

    /**
     * 根据学期，教师查询课程表
     * @param term
     * @param teacherNo
     * @return
     */
    @SaCheckRole("ROLE_TEACHER")
    @GetMapping("/teacher")
    public Result queryScheduleByTeacherNo(@RequestParam("term") String term,@RequestParam("teacherNo") String teacherNo) {
        List<ScheduleDTO> scheduleList = scheduleService.queryScheduleByTeacherNo(term,teacherNo);
        return Result.success(scheduleList);
    }
    @SaCheckRole("ROLE_TEACHER")
    //通过学期查询所任课班级
    @GetMapping("/term")
    public Result selectDistinctClassesByTermAndTeacher(@RequestParam("term") String term,@RequestParam("teacherNo") String teacherNo) {
        return Result.success(scheduleService.selectDistinctClassesByTermAndTeacher(term,teacherNo));
    }


    //@SaCheckRole("ROLE_ADMIN")
    @GetMapping("/courseTime/{id}")
    //获取课程可修改的时间列表，通过查询班级空余时间和老师空余时间
    public Result getCourseTimeList(@PathVariable Integer id) {
        Schedule schedule = scheduleService.getById(id);
        String classNo=schedule.getClassNo();
        String teacherNo=schedule.getTeacherNo();
        return Result.success(scheduleService.getCourseTimeList(classNo,teacherNo));
    }

    //@SaCheckRole("ROLE_ADMIN")
    @GetMapping("/classroom/{id}")
    public Result getClassroomList(@PathVariable Integer id) {
        Schedule schedule = scheduleService.getById(id);
        String courseTime = schedule.getCourseTime();
        String classroomNo = schedule.getClassroomNo();
        return Result.success(scheduleService.getClassroomList(classroomNo,courseTime));
    }

    //新增或者更新
    @PostMapping
    public Result save(@RequestBody Schedule schedule) {
        scheduleService.saveOrUpdate(schedule);
        return Result.success();
    }
}