package com.chen.coursearrangement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.coursearrangement.entity.Schedule;
import com.chen.coursearrangement.entity.dto.ScheduleDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Jack Chen
 * @since 2024-01-12
 */
public interface ScheduleMapper extends BaseMapper<Schedule> {
    //插入课程计划
    @Insert("insert into schedule(grade_no, class_no, course_no, teacher_no, classroom_no, course_time, term) values(#{gradeNo}, #{classNo}, #{courseNo}, #{teacherNo}, #{classroomNo}, #{courseTime}, #{term})")
    void insertSchedule(@Param("gradeNo") String gradeNo, @Param("classNo") String classNo, @Param("courseNo") String courseNo,
                          @Param("teacherNo") String teacherNo, @Param("classroomNo") String classroomNo, @Param("courseTime") String courseTime, @Param("term") String term);
    //按照学期删除课程计划
    @Delete("DELETE FROM schedule WHERE term = #{term}")
    int deleteScheduleByTerm(@Param("term") String term);

    //查询班级课表
    List<ScheduleDTO> queryScheduleByClassNo( @Param("term") String term, @Param("classNo")String classNo);
    //查询教师课表
    List<ScheduleDTO> queryScheduleByTeacherNo(@Param("term")String term, @Param("teacherNo")String teacherNo);

    //通过学期和教师编号查询该学期的有课班级
    @MapKey("classNo")
    List<Map<String, Object>> selectDistinctClassesByTermAndTeacher( @Param("term") String term,  @Param("teacherNo") String teacherNo);

    List<String> getCourseTimeList(@Param("classNo")String classNo, @Param("teacherNo")String teacherNo);

    @MapKey("classroomNo")
    List<Map<String, String>> getClassroomList(@Param("classroomNo")String classroomNo,@Param("courseTime")String courseTime);
}
