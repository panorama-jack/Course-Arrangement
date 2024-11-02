package com.chen.coursearrangement.entity.dto;
import lombok.Data;


//返回课程的课程编号，课程名和任课教师
@Data
public class CoursePlanDTO {
    private String courseNo;
    private String courseName;
    private String teacherName;
}
