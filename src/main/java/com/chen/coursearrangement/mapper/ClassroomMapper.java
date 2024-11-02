package com.chen.coursearrangement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.coursearrangement.entity.Classroom;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Jack Chen
 * @since 2023-05-19
 */
public interface ClassroomMapper extends BaseMapper<Classroom> {

    //  查询某个教学楼下的教室列表    在查询语句中使用IN条件来匹配多个值。
    List<Classroom> selectByTeachingBuildingNos(@Param("teachingBuildingNos") List<String> teachingBuildingNos);

    Page<Classroom> findPage(Page<Classroom> page, @Param("teachingBuildingNo") String teachingBuildingNo, @Param("classroomType") String classroomType);

    // 获取班级编号最后三位
    @Select("SELECT MAX(CAST(SUBSTRING(classroom_no, -2) AS UNSIGNED)) AS max_three_two_digits FROM classroom WHERE teaching_building_no = #{teachingBuildingNo} AND classroom_type=#{classroomType};")
    Integer lastTwoDigits(@Param("teachingBuildingNo")String teachingBuildingNo, @Param("classroomType") String classroomType);

}
