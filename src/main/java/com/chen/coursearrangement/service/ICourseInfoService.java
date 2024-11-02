package com.chen.coursearrangement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.coursearrangement.entity.CourseInfo;
import com.chen.coursearrangement.entity.dto.CourseInfoDTO;
import com.chen.coursearrangement.entity.dto.CoursePlanDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jack Chen
 * @since 2024-01-17
 */
public interface ICourseInfoService extends IService<CourseInfo> {
    List<CoursePlanDTO> findCourse();

    int lastThreeDigits(String college, String courseAttribute);

    List<CourseInfo> export(String college, String courseAttribute, String courseName);

    int imp(List<CourseInfoDTO> list);
}
