package com.chen.coursearrangement.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.coursearrangement.common.ConstantInfo;
import com.chen.coursearrangement.common.Result;
import com.chen.coursearrangement.entity.Dict;
import com.chen.coursearrangement.entity.Menu;
import com.chen.coursearrangement.mapper.DictMapper;
import com.chen.coursearrangement.service.IMenuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 *
 * @author Jack Chen
 * @since 2023-05-16
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private IMenuService menuService;

    @Resource
    private DictMapper dictMapper;

    //新增或者更新
    @PostMapping
    public Result save(@RequestBody Menu menu) {
        menuService.saveOrUpdate(menu);
        return Result.success();
    }

    //删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        menuService.removeById(id);
        return Result.success();
    }

    //获取所有菜单的列表。然后，使用Java 8的Stream API将菜单列表转换为包含所有菜单ID的Stream。
    @GetMapping("/ids")
    public Result findAllIds() {
        return Result.success(menuService.list().stream().map(Menu::getId));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {//批量删除
        menuService.removeByIds(ids);
        return Result.success();
    }

    //查询所有数据
    @GetMapping
    public Result findAll(@RequestParam(defaultValue = "") String name) {
        return Result.success(menuService.findMenus(name));
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(menuService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam String name, @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        queryWrapper.orderByDesc("id");
        return Result.success(menuService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    //获取图标
    @GetMapping("/icons")
    public Result getIcons() {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", ConstantInfo.DICT_TYPE_ICON);
        return Result.success(dictMapper.selectList(queryWrapper));
    }
}
