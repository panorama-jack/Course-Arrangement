package com.chen.coursearrangement.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.coursearrangement.common.Result;
import com.chen.coursearrangement.entity.Dict;
import com.chen.coursearrangement.service.IDictService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @author Jack Chen
 * @since 2024-01-19
 */
@RestController
@RequestMapping("/dict")
public class DictController {

    @Resource
    private IDictService dictService;

    //新增或者更新
    @SaCheckRole("ROLE_ADMINISTRATOR")
    @PostMapping
    public Result save(@RequestBody Dict dict) {
        dictService.saveOrUpdate(dict);
        return Result.success();
    }

    //删除
    @SaCheckRole("ROLE_ADMINISTRATOR")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        dictService.removeById(id);
        return Result.success();
    }
    @SaCheckRole("ROLE_ADMINISTRATOR")
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {//批量删除
        dictService.removeByIds(ids);
        return Result.success();
    }

    //查询所有数据
    @SaCheckRole("ROLE_ADMINISTRATOR")
    @GetMapping
    public Result findAll() {
        return Result.success(dictService.list());
    }

    //获取数据字典的字典类型列表
    @SaCheckRole("ROLE_ADMINISTRATOR")
    @GetMapping("/selectType")
    public Result selectType() {
        return Result.success(dictService.selectType());
    }

    //通过字典列表进行查询
    @GetMapping("/{type}")
    public Result findType(@PathVariable String type) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type);
        return Result.success(dictService.list(queryWrapper));
    }
    @SaCheckRole("ROLE_ADMINISTRATOR")
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String type) {
        return Result.success(dictService.findPage(new Page<>(pageNum, pageSize), type));
    }
}
