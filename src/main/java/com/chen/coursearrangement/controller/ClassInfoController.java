package com.chen.coursearrangement.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.coursearrangement.common.ConstantInfo;
import com.chen.coursearrangement.common.Result;
import com.chen.coursearrangement.entity.ClassInfo;
import com.chen.coursearrangement.entity.dto.UserDTO;
import com.chen.coursearrangement.service.IClassInfoService;
import com.chen.coursearrangement.service.IUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jack Chen
 * @since 2024-01-17
 */
@RestController
@RequestMapping("/classInfo")
public class ClassInfoController {
    @Resource
    private IClassInfoService classInfoService;
    @Resource
    private IUserService userService;

    //新增或者更新
    @PostMapping
    public Result save(@RequestBody ClassInfo classInfo) {
        UserDTO userInfo = userService.getUserRoleAndCollege(StpUtil.getLoginIdAsString());
        String role = userInfo.getRole();
        String college = userInfo.getCollege();
        //系统管理员有权限操作所有班级，或者管理员有权限操作本学院班级
        if (ConstantInfo.ROLE_ADMINISTRATOR.equals(role) || (ConstantInfo.ROLE_ADMIN.equals(role) && college.equals(classInfo.getCollegeNo()))){
            // 若为新增班级，设置班级人数为0
            if(classInfo.getId()==null){
                classInfo.setStudentNumber(0);}
            classInfoService.saveOrUpdate(classInfo);
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
        ClassInfo classInfo = classInfoService.getById(id);
        if (classInfo == null) {
            return Result.error(ConstantInfo.CODE_404, "班级不存在");
        }
        // 系统管理员有权限操作所有班级，或者管理员有权限操作本学院班级
        if (ConstantInfo.ROLE_ADMINISTRATOR.equals(role) || (ConstantInfo.ROLE_ADMIN.equals(role) && college.equals(classInfo.getCollegeNo()))) {
            classInfoService.removeById(id);
            return Result.success();
        } else {
            // 其他角色无权限
            return Result.error(ConstantInfo.CODE_401, "权限不足");
        }
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        UserDTO userInfo = userService.getUserRoleAndCollege(StpUtil.getLoginIdAsString());
        String role = userInfo.getRole();
        String college = userInfo.getCollege();
        // 系统管理员有权限操作所有班级，管理员有权限操作本学院班级
        if (ConstantInfo.ROLE_ADMINISTRATOR.equals(role)) {
            classInfoService.removeByIds(ids);
            return Result.success();
        } else if(ConstantInfo.ROLE_ADMIN.equals(role)){
            for (int id:ids) {
                ClassInfo classInfo = classInfoService.getById(id);
                if(classInfo!=null&&college.equals(classInfo.getCollegeNo())){
                    classInfoService.removeById(id);
                }
            }
            return Result.success();
        }else {
            // 其他角色无权限
            return Result.error(ConstantInfo.CODE_401, "权限不足");
        }
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String college,
                           @RequestParam(defaultValue = "") String className) {
        UserDTO userInfo = userService.getUserRoleAndCollege(StpUtil.getLoginIdAsString());

        String role = userInfo.getRole();
        if (ConstantInfo.ROLE_ADMIN.equals(role)) {
            // 对管理员进行权限控制
            college = userInfo.getCollege();
        }
        QueryWrapper<ClassInfo> queryWrapper = new QueryWrapper<>();
        // 精确查询
        if (!StringUtils.isEmpty(college)) {
            queryWrapper.eq("college_no", college);
        }
        // 模糊查询班级名称
        if (!StringUtils.isEmpty(className)) {
            queryWrapper.like("class_name", className);
        }
        queryWrapper.orderByAsc("id");

        // 分页查询并返回结果
        return Result.success(classInfoService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @GetMapping
    public Result findAll() {
        return Result.success(classInfoService.list());
    }

    //通过年级查询班级
    @GetMapping("/grade/{grade}")
    public Result selectByGrade(@PathVariable String grade) {
        QueryWrapper<ClassInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("grade_no", grade);
        return Result.success(classInfoService.list(queryWrapper));
    }

    //通过学院查询班级
    @GetMapping("/college/{college}")
    public Result selectByCollege(@PathVariable String college) {
        QueryWrapper<ClassInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("college_no", college);
        return Result.success(classInfoService.list(queryWrapper));
    }


    //通过年级和学院查询班级
    @GetMapping("/gradeAndCollege")
    public Result gradeAndCollege(@RequestParam String grade, @RequestParam String college) {
        QueryWrapper<ClassInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("grade_no", grade);
        queryWrapper.eq("college_no", college);
        return Result.success(classInfoService.list(queryWrapper));
    }

    //生成班级编号
    @PostMapping("/generateClassNo")
    public Result generateSClassNo(@RequestBody Map<String, Object> requestData) {
        // 获取 form 中的参数
        String gradeNo = String.valueOf(requestData.get("gradeNo"));
        String collegeNo = String.valueOf(requestData.get("college"));
        String majorNo = String.valueOf(requestData.get("major"));
        // 组装字符串
        String studentNoPrefix = gradeNo + collegeNo + majorNo;
        //通过统计已经录入系统的该专业班级个数，得到班级编号后两位
        int lastTwoDigits = classInfoService.lastTwoDigits(gradeNo,majorNo);
        String classNo = studentNoPrefix + String.format("%02d", lastTwoDigits + 1);
        return Result.success("班级编号生成成功！", classNo);
    }
}
