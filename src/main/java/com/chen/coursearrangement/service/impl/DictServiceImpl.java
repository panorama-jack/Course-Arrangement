package com.chen.coursearrangement.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.coursearrangement.entity.Dict;
import com.chen.coursearrangement.mapper.DictMapper;
import com.chen.coursearrangement.service.IDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Jack Chen
 * @since 2024-01-19
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {
    @Resource
    DictMapper dictMapper;

    @Override
    public List<String> selectType() {
        return dictMapper.selectType();
    }

    @Override
    public Page<Dict> findPage(Page<Dict> page, String type) {
        return dictMapper.findPage(page, type);
    }

    @Override
    public List<Map<String, Object>> queryTypeCount() {
       return dictMapper.queryTypeCount();
    }
}
