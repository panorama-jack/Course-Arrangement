package com.chen.coursearrangement.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.coursearrangement.common.Result;
import com.chen.coursearrangement.entity.Role;
import com.chen.coursearrangement.service.IRoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @author Jack Chen
 * @since 2023-05-17
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private IRoleService roleService;

    //新增或者更新
    @PostMapping
    public Result save(@RequestBody Role role) {
        roleService.saveOrUpdate(role);
        return Result.success();
    }

    //删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        roleService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {//批量删除
        roleService.removeByIds(ids);
        return Result.success();
    }

    //查询所有数据
    @GetMapping
    public Result findAll() {
        return Result.success(roleService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(roleService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam String name,
                           @RequestParam Integer pageNum, @RequestParam Integer pageSize){
        QueryWrapper<Role> queryWrapper=new QueryWrapper<>();
        queryWrapper.like("name",name);
        queryWrapper.orderByDesc("id");
        return Result.success(roleService.page(new Page<>(pageNum,pageSize),queryWrapper));
    }

    /**
     * 绑定角色和菜单的关系
     * @param roleId  角色id
     * @param menuIds  菜单id数组
     * @return
     */
    @PostMapping("/roleMenu/{roleId}")
    public Result save(@PathVariable Integer roleId,@RequestBody List<Integer> menuIds) {
        roleService.saveRoleMenu(roleId,menuIds);
        return Result.success();
    }

    //查询某一角色的菜单列表
    @GetMapping("/roleMenu/{roleId}")
    public Result getRoleMenu(@PathVariable Integer roleId) {
        return Result.success(roleService.getRoleMenu(roleId));
    }
}
