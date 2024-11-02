package com.chen.coursearrangement.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.coursearrangement.common.ConstantInfo;
import com.chen.coursearrangement.common.Result;
import com.chen.coursearrangement.entity.Notification;
import com.chen.coursearrangement.entity.dto.UserDTO;
import com.chen.coursearrangement.service.INotificationService;
import com.chen.coursearrangement.service.IUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Jack Chen
 * @since 2023-05-20
 */
@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Resource
    private INotificationService notificationService;

    @Resource
    private IUserService userService;

    // 新增或者更新通知
    @PostMapping
    public Result saveOrUpdate(@RequestBody Notification notification) {

        UserDTO userInfo = userService.getUserRoleAndCollege(StpUtil.getLoginIdAsString());
        // 获取用户角色
        String role = userInfo.getRole();
        // 系统管理员可以修改全部的，管理员只可以修改自己的
        if (ConstantInfo.ROLE_ADMIN.equals(role)&& StpUtil.getLoginIdAsString().equals( notification.getUser() )||ConstantInfo.ROLE_ADMINISTRATOR.equals(role)) {
            // 设置通知的时间和发布人
            if (notification.getId() == null) {
                // 新增通知
                notification.setTime(DateUtil.now());
                notification.setUser(StpUtil.getLoginIdAsString());
            }
            // 保存或更新通知
            notificationService.saveOrUpdate(notification);
            return Result.success();
        }else{
            return Result.error(ConstantInfo.CODE_401, "权限不足");
        }
    }

    //删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        UserDTO userInfo = userService.getUserRoleAndCollege(StpUtil.getLoginIdAsString());
        String role = userInfo.getRole();
        String college = userInfo.getCollege();
        Notification notification = notificationService.getById(id);
        if (notification == null) {
            return Result.error(ConstantInfo.CODE_404, "通知不存在");
        }
        //系统管理员有权限操作所有通知，或者管理员有权限操作自己的通知
        if (ConstantInfo.ROLE_ADMIN.equals(role)&& StpUtil.getLoginIdAsString().equals( notification.getUser() )||ConstantInfo.ROLE_ADMINISTRATOR.equals(role))  {
            notificationService.removeById(id);
            return Result.success();
        } else {
            // 其他角色无权限
            return Result.error(ConstantInfo.CODE_401, "权限不足");
        }
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {//批量删除
        UserDTO userInfo = userService.getUserRoleAndCollege(StpUtil.getLoginIdAsString());
        String role = userInfo.getRole();

        // 系统管理员有权限操作所有班级，管理员有权限操作本学院班级
        if (ConstantInfo.ROLE_ADMINISTRATOR.equals(role)) {
            notificationService.removeByIds(ids);
            return Result.success();
        } else if(ConstantInfo.ROLE_ADMIN.equals(role)){
            for (int id:ids) {
                Notification notification = notificationService.getById(id);
                if(notification!=null&& StpUtil.getLoginIdAsString().equals( notification.getUser() )){
                    notificationService.removeById(id);
                }
            }
            return Result.success();
        }else {
            // 其他角色无权限
            return Result.error(ConstantInfo.CODE_401, "权限不足");
        }
    }

    //首页通知详情查询
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(notificationService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam String title,
            @RequestParam String college) {
        UserDTO userInfo = userService.getUserRoleAndCollege(StpUtil.getLoginIdAsString());
        String role = userInfo.getRole();

        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");

        // 如果是系统管理员，可以查询所有数据
        if (ConstantInfo.ROLE_ADMINISTRATOR.equals(role)) {
            //精确查询
            if (StrUtil.isNotBlank(college)) {
                queryWrapper.eq("college_no", college);
            }
        } else if (ConstantInfo.ROLE_ADMIN.equals(role)) {
            // 如果是管理员，追加查询条件
            college = userInfo.getCollege();
            if (!StringUtils.isEmpty(college)) {
                queryWrapper.eq("college_no", college);
            }
            // 追加针对管理员的查询条件
            queryWrapper.or(wrapper ->
                    wrapper.eq("college_no", "0")
                            .or()
                            .eq("college_no", "1")
            );
        } else {
            college = userInfo.getCollege();
            // 如果是老师或者学生，追加查询条件
            if (!StringUtils.isEmpty(college)) {
                queryWrapper.eq("college_no", college);
            }
            // 追加针对老师或学生的查询条件   1为全体通知，0为管理员通知
            queryWrapper.or(wrapper ->
                    wrapper.eq("college_no", "1")
            );
        }

        //模糊查询
        if (StrUtil.isNotBlank(title)) {
            queryWrapper.like("title", title);
        }
        return Result.success(notificationService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @SaCheckRole("ROLE_ADMINISTRATOR")
    @GetMapping("/administrator/page")
    public Result findAdministratorPage(
            @RequestParam String title,
            @RequestParam String college,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize
    ) {
        UserDTO userInfo = userService.getUserRoleAndCollege(StpUtil.getLoginIdAsString());
        String role = userInfo.getRole();

        // 判断用户角色，只有 Administrator 才能访问
        if (!ConstantInfo.ROLE_ADMINISTRATOR.equals(role)) {
            return Result.error(ConstantInfo.CODE_401, "权限不足");
        }
        // 如果是 Administrator 角色，进行查询操作
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();

        if (StrUtil.isNotBlank(college)) {
            queryWrapper.like("college_no", college);
        }

        //模糊查询
        if (StrUtil.isNotBlank(title)) {
            queryWrapper.like("title", title);
        }
        queryWrapper.orderByDesc("id");
        return Result.success(notificationService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @SaCheckRole("ROLE_ADMIN")
    @GetMapping("/admin/page")
    public Result findAdminPage(
            @RequestParam String title,
            @RequestParam String college,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize
    ) {
        UserDTO userInfo = userService.getUserRoleAndCollege(StpUtil.getLoginIdAsString());
        String role = userInfo.getRole();
        // 判断用户角色，只有 admin 才能访问
        if (!ConstantInfo.ROLE_ADMIN.equals(role)) {
            return Result.error(ConstantInfo.CODE_401, "权限不足");
        }
        // 如果是 admin 角色，进行查询操作，只查询自己发布的通知
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user",StpUtil.getLoginIdAsString()); // 只查询自己发布的通知
        //模糊查询
        if (StrUtil.isNotBlank(title)) {
            queryWrapper.like("title", title);
        }
        queryWrapper.orderByDesc("id");
        return Result.success(notificationService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}
