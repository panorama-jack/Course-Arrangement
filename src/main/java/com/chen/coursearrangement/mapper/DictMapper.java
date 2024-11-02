package com.chen.coursearrangement.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.coursearrangement.entity.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Jack Chen
 * @since 2024-01-19
 */
public interface DictMapper extends BaseMapper<Dict> {
   //查询字典类型列表
    @Select("SELECT DISTINCT type FROM sys_dict")
    List<String> selectType();

    Page<Dict> findPage(Page<Dict> page, @Param("type")String type);
    //echart饼图查询语句
    List<Map<String, Object>> queryTypeCount();
}
