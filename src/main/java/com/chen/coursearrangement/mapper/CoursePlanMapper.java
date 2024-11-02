package com.chen.coursearrangement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.coursearrangement.entity.CoursePlan;
import com.chen.coursearrangement.entity.dto.EchartsDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Jack Chen
 * @since 2024-01-19
 */
public interface CoursePlanMapper extends BaseMapper<CoursePlan> {
    //查询开课班级列表
    @Select("SELECT distinct class_no FROM course_plan")
    List<String> selectClassNo();
    //echart堆叠图查询语句
    List<EchartsDTO> queryCoursePlanProcess(@Param("term")String term, @Param("gradeNo")String gradeNo);
}
