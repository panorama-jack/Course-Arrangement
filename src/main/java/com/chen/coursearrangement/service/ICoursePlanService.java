package com.chen.coursearrangement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.coursearrangement.entity.CoursePlan;
import com.chen.coursearrangement.entity.dto.EchartsDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jack Chen
 * @since 2024-01-19
 */
public interface ICoursePlanService extends IService<CoursePlan> {

    String courseScheduling(String term);

    List<EchartsDTO> queryCoursePlanProcess(String term, String gradeNo);
}
