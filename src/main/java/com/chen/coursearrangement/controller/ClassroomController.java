package com.chen.coursearrangement.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.coursearrangement.common.Result;
import com.chen.coursearrangement.entity.Classroom;
import com.chen.coursearrangement.service.IClassroomService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jack Chen
 * @since 2023-05-19
 */
@RestController
@RequestMapping("/classroom")
public class ClassroomController {

    @Resource
    private IClassroomService classroomService;

    //新增或者更新
    @PostMapping
    public Result save(@RequestBody Classroom classroom) {
        classroomService.saveOrUpdate(classroom);
        return Result.success();
    }

    //删除
    @DeleteMapping("/{id}")

    public Result delete(@PathVariable Integer id) {
        classroomService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {//批量删除
        classroomService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String teachingBuilding,
                           @RequestParam(defaultValue = "") String classroomType) {
        QueryWrapper<Classroom> queryWrapper = new QueryWrapper<>();
        // 精确查询
        if (!StringUtils.isEmpty(teachingBuilding)) {
            queryWrapper.eq("teaching_building_no", teachingBuilding);
        }
        // 精确查询
        if (!StringUtils.isEmpty(classroomType)) {
            queryWrapper.eq("classroom_type", classroomType);
        }
        queryWrapper.orderByAsc("id");
        return Result.success(classroomService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    //生成教室编号
    @PostMapping("/generateClassroomNo")
    public Result generateStudentNo(@RequestBody Map<String, Object> requestData) {
        // 获取 form 中的参数
        String teachingBuildingNo = String.valueOf(requestData.get("teachingBuildingNo"));
        String classroomType = String.valueOf(requestData.get("classroomType"));
        String classroomNoPrefix = teachingBuildingNo + classroomType;
        //通过查询数据库中同一教学楼同一类型教室的个数，得到教室编号后两位
        int lastTwoDigits = classroomService.lastTwoDigits(teachingBuildingNo, classroomType);
        String classroomNo = classroomNoPrefix + String.format("%02d", lastTwoDigits + 1);
        return Result.success("教室编号生成成功！",classroomNo);
    }
}
