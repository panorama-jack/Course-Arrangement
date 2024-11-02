package com.chen.coursearrangement.service.impl;

import com.chen.coursearrangement.entity.ClassInfo;
import com.chen.coursearrangement.mapper.ClassInfoMapper;
import com.chen.coursearrangement.service.IClassInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jack Chen
 * @since 2024-01-17
 */
@Service
public class ClassInfoServiceImpl extends ServiceImpl<ClassInfoMapper, ClassInfo> implements IClassInfoService {
    @Resource
    private ClassInfoMapper classInfoMapper;

    @Override
    public int lastTwoDigits(String gradeNo, String majorNo) {
        Integer result = classInfoMapper.lastTwoDigits(gradeNo, majorNo);
        return result != null ? result : 0;
    }
}
