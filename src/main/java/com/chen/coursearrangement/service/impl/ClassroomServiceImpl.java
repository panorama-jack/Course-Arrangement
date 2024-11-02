package com.chen.coursearrangement.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.coursearrangement.entity.Classroom;
import com.chen.coursearrangement.mapper.ClassroomMapper;
import com.chen.coursearrangement.service.IClassroomService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jack Chen
 * @since 2023-05-19
 */
@Service
public class ClassroomServiceImpl extends ServiceImpl<ClassroomMapper, Classroom> implements IClassroomService {

    @Resource
    private ClassroomMapper classroomMapper;

    @Override
    public Page<Classroom> findPage(Page<Classroom> page, String teachingBuilding, String type) {
        return classroomMapper.findPage(page, teachingBuilding, type);
    }

    @Override
    public int lastTwoDigits(String teachingBuildingNo, String classroomType) {
        Integer result = classroomMapper.lastTwoDigits(teachingBuildingNo, classroomType);
        return result != null ? result : 0;
    }

}
