package com.chen.coursearrangement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.coursearrangement.entity.Dict;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jack Chen
 * @since 2024-01-19
 */
public interface IDictService extends IService<Dict> {

    List<String> selectType();

    Page<Dict>  findPage(Page<Dict> page, String type);

    List<Map<String, Object>> queryTypeCount();


}
