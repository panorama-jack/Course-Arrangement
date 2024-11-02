package com.chen.coursearrangement.controller;


import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.coursearrangement.common.ConstantInfo;
import com.chen.coursearrangement.common.Result;
import com.chen.coursearrangement.entity.CoursePlan;
import com.chen.coursearrangement.entity.dto.UserDTO;
import com.chen.coursearrangement.service.ICoursePlanService;
import com.chen.coursearrangement.service.IUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @author Jack Chen
 * @since 2024-01-19
 */
@RestController
@RequestMapping("/coursePlan")
public class CoursePlanController {

    @Resource
    private ICoursePlanService coursePlanService;

    @Resource
    private IUserService userService;

    //新增或者更新
    @PostMapping
    public Result save(@RequestBody CoursePlan coursePlan) {
        UserDTO userInfo = userService.getUserRoleAndCollege(StpUtil.getLoginIdAsString());
        String role = userInfo.getRole();
        String college = userInfo.getCollege();
        // 系统管理员有权限操作所有课程计划，或者管理员有权限操作本学院课程计划
        if (ConstantInfo.ROLE_ADMINISTRATOR.equals(role) || (ConstantInfo.ROLE_ADMIN.equals(role) && college.equals(coursePlan.getCollegeNo()))) {
            coursePlanService.saveOrUpdate(coursePlan);
            return Result.success();
        } else {
            // 其他角色无权限
            return Result.error(ConstantInfo.CODE_401, "权限不足");
        }
    }

    //删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        UserDTO userInfo = userService.getUserRoleAndCollege(StpUtil.getLoginIdAsString());
        String role = userInfo.getRole();
        String college = userInfo.getCollege();
        CoursePlan coursePlan = coursePlanService.getById(id);
        if (coursePlan == null) {
            return Result.error(ConstantInfo.CODE_404, "课程计划不存在");
        }
        if (ConstantInfo.ROLE_ADMINISTRATOR.equals(role) || (ConstantInfo.ROLE_ADMIN.equals(role) && college.equals(coursePlan.getCollegeNo()))) {
            // 系统管理员有权限操作所有课程计划，或者管理员有权限操作本学院课程计划
            coursePlanService.removeById(id);
            return Result.success();
        } else {
            // 其他角色无权限
            return Result.error(ConstantInfo.CODE_401, "权限不足");
        }
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        UserDTO userInfo = userService.getUserRoleAndCollege(StpUtil.getLoginIdAsString());
        if (userInfo == null) {
            return Result.error(ConstantInfo.CODE_404, "用户信息获取失败");
        }

        String role = userInfo.getRole();
        String college = userInfo.getCollege();
        if (ConstantInfo.ROLE_ADMINISTRATOR.equals(role) ) {
            // 系统管理员有权限操作所有课程计划，管理员有权限操作本学院课程计划
            coursePlanService.removeByIds(ids);
            return Result.success();
        }else if(ConstantInfo.ROLE_ADMIN.equals(role)){
            for (int id:ids) {
                CoursePlan coursePlan = coursePlanService.getById(id);
                if(coursePlan!=null&&college.equals(coursePlan.getCollegeNo())){
                    coursePlanService.removeById(id);
                }
            }
            return Result.success();
        } else {
            // 其他角色无权限
            return Result.error(ConstantInfo.CODE_401, "权限不足");
        }
    }


    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String term,
                           @RequestParam(defaultValue = "") String college,
                           @RequestParam(defaultValue = "") String grade,
                           @RequestParam(defaultValue = "") String classNo) {
        UserDTO userInfo = userService.getUserRoleAndCollege(StpUtil.getLoginIdAsString());
        String role = userInfo.getRole();
        QueryWrapper<CoursePlan> queryWrapper = new QueryWrapper<>();

        // 对管理员进行权限控制
        if (ConstantInfo.ROLE_ADMIN.equals(role)) {
            // 如果是 ROLE_ADMIN，使用当前用户的学院信息
            college = userInfo.getCollege();
        }

        // 精确查询
        if (!StringUtils.isEmpty(term)) {
            queryWrapper.eq("term", term);
        }
        // 精确查询
        if (!StringUtils.isEmpty(grade)) {
            queryWrapper.eq("grade_no", grade);
        }

        // 精确查询
        if (!StringUtils.isEmpty(college)) {
            queryWrapper.eq("college_no", college);
        }
        // 精确查询
        if (!StringUtils.isEmpty(classNo)) {
            queryWrapper.eq("class_no", classNo);
        }

        queryWrapper.orderByDesc("id");
        return Result.success(coursePlanService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }


    /**
     * 排课算法接口，传入学期开始去查对应学期的开课任务，进行排课，
     *
     * @param term
     * @return
     */
    @SaCheckRole("ROLE_ADMINISTRATOR")
    @GetMapping("/arrange/{term}")
    public Result arrange(@PathVariable("term") String term) {
        return Result.success(coursePlanService.courseScheduling(term));
    }
}
