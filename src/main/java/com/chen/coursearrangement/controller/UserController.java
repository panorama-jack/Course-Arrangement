package com.chen.coursearrangement.controller;


import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.coursearrangement.common.ConstantInfo;
import com.chen.coursearrangement.common.Result;
import com.chen.coursearrangement.entity.User;
import com.chen.coursearrangement.service.IUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 *
 * @author Jack Chen
 * @since 2023-05-15
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        String userNo = user.getUserNo();
        String password = user.getPassword();
        if (StrUtil.isBlank(userNo) || StrUtil.isBlank(password)) {
            return Result.error(ConstantInfo.CODE_400, "参数错误");
        }
        User userLogin = userService.login(user);
        return Result.success(userLogin);
    }

    @GetMapping("/logout")
    public Result logout(){
        // 当前会话注销登录
        StpUtil.logout();
        return Result.success();
    }

    //新增或者更新   修改电话号码
    @PostMapping
    public Result save(@RequestBody User user) {
        userService.saveOrUpdate(user);
        if(ConstantInfo.ROLE_STUDENT.equals(user.getRole())){
            userService.editStudentTelephone(user.getUserNo(),user.getTelephone());
        }else{
            userService.editTeacherTelephone(user.getUserNo(),user.getTelephone());
        }
        return Result.success();
    }

    @GetMapping("/userNo/{userNo}")    //通过账号查询
    public Result findByUsername(@PathVariable String userNo) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_no", userNo);
        User user = userService.getOne(queryWrapper);
        user.setTelephone(userService.getUserTelephone(userNo));
        return Result.success(user);
    }

    //修改用户密码
    @PostMapping("/password")
    public Result password(@RequestBody User user) {
        userService.updatePassword(user);
        return Result.success();
    }

    @GetMapping("/page")
    @SaCheckRole("ROLE_ADMINISTRATOR")
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String role, @RequestParam(defaultValue = "") String userNo) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 精确查询角色
        if (!StringUtils.isEmpty(role)) {
            queryWrapper.eq("role", role);
        }
        // 模糊查询用户账号
        if (!StringUtils.isEmpty(userNo)) {
            queryWrapper.like("user_no", userNo);
        }
        queryWrapper.orderByAsc("id");
        return Result.success(userService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    //获取用户的学院
    @GetMapping("/getUserCollege/{userNo}")
    public Result getUserCollege(@PathVariable String userNo) {
        return Result.success(userService.getUserCollege(userNo));
    }

    //修改用户的角色
    @GetMapping("/editUserRole")
    @SaCheckRole("ROLE_ADMINISTRATOR")
    public Result editUserRole(@RequestParam Integer id,
                               @RequestParam String role) {
        return Result.success(userService.editUserRole(id, role));
    }

    //重置密码
    @GetMapping("/resetPassword/{userNo}")
    @SaCheckRole("ROLE_ADMINISTRATOR")
    public Result resetPassword(@PathVariable String userNo) {
        return Result.success(userService.resetPassword(userNo));
    }

    //获得用户的角色和学院
    @GetMapping("/getUserRoleAndCollege/{userNo}")
    public Result getUserRoleAndCollege(@PathVariable String userNo) {
        return Result.success(userService.getUserRoleAndCollege(userNo));
    }

}
