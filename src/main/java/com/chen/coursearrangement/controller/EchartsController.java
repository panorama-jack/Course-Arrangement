package com.chen.coursearrangement.controller;


import cn.hutool.core.collection.CollUtil;
import com.chen.coursearrangement.common.Result;
import com.chen.coursearrangement.service.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Resource
    private IStudentService studentService;
    @Resource
    private ITeacherService teacherService;
    @Resource
    private IClassInfoService classInfoService;
    @Resource
    private ICourseInfoService courseInfoService;
    @Resource
    private IDictService dictService;
    @Resource
    private ICoursePlanService coursePlanService;

    //首页数据获取
    @GetMapping("/count")
    public Result get() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", CollUtil.newArrayList("学生人数", "教师人数", "班级总数", "课程总数"));
        map.put("value", CollUtil.newArrayList(studentService.count(), teacherService.count(), classInfoService.count(), courseInfoService.count()));
        return Result.success(map);
    }

    // 查询不同 type 的数量
    @GetMapping("/queryTypeCount")
    public Result queryTypeCount() {
        return Result.success(dictService.queryTypeCount());
    }

    @GetMapping("/queryCoursePlanProcess")
    public Result queryCoursePlanProcess(
            @RequestParam(defaultValue = "") String term, @RequestParam(defaultValue = "") String gradeNo) {
        return Result.success(coursePlanService.queryCoursePlanProcess(term,gradeNo));
    }
}