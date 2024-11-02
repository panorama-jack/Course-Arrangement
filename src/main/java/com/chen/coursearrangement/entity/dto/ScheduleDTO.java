package com.chen.coursearrangement.entity.dto;

import lombok.Data;

//课表查询返回信息
@Data
public class ScheduleDTO{
    private Integer id;
    private String courseTime;
    private String classroomName;
    private String courseName;
    private String teacherName;
    //上课班级名称
    private String className;
}
