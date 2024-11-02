package com.chen.coursearrangement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.coursearrangement.entity.Classroom;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jack Chen
 * @since 2023-05-19
 */
public interface IClassroomService extends IService<Classroom> {
    Page<Classroom> findPage(Page<Classroom> page, String teachingBuilding, String classroomType);

    int lastTwoDigits(String teachingBuildingNo, String classroomType);
}
