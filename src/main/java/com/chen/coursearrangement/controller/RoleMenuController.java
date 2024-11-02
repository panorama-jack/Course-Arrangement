package com.chen.coursearrangement.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.coursearrangement.common.Result;
import com.chen.coursearrangement.entity.RoleMenu;
import com.chen.coursearrangement.service.IRoleMenuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @author Jack Chen
 * @since 2023-05-17
 */
@RestController
@RequestMapping("/role-menu")
public class RoleMenuController {

@Resource
private IRoleMenuService roleMenuService;

//新增或者更新
@PostMapping
public Result save(@RequestBody RoleMenu roleMenu) {
    roleMenuService.saveOrUpdate(roleMenu);
        return Result.success();
        }

//删除
@DeleteMapping("/{id}")
public Result delete(@PathVariable Integer id) {
    roleMenuService.removeById(id);
        return Result.success();
        }

@PostMapping("/del/batch")
public Result deleteBatch(@RequestBody List<Integer> ids) {//批量删除
    roleMenuService.removeByIds(ids);
        return Result.success();
        }

//查询所有数据
@GetMapping
public Result findAll() {
        return Result.success(roleMenuService.list());
        }

@GetMapping("/{id}")
public Result findOne(@PathVariable Integer id) {
        return Result.success(roleMenuService.getById(id));
        }

@GetMapping("/page")
public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize){
        QueryWrapper<RoleMenu> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(roleMenuService.page(new Page<>(pageNum,pageSize),queryWrapper));
        }
}
