package com.chen.coursearrangement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.coursearrangement.entity.ClassInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Jack Chen
 * @since 2024-01-17
 */
public interface ClassInfoMapper extends BaseMapper<ClassInfo> {

    // 获得班级的人数
    @Select("SELECT student_number FROM class_info WHERE class_no = #{classNo}")
    int selectStuNum(@Param("classNo") String classNo);

    //获取班级编号最后两位
    @Select("SELECT MAX(CAST(SUBSTRING(class_no, -2) AS UNSIGNED)) AS max_last_two_digits FROM class_info WHERE major_no = #{majorNo} AND grade_no = #{gradeNo}")
    Integer lastTwoDigits(@Param("gradeNo")String gradeNo, @Param("majorNo")String majorNo);

}
