package com.chen.coursearrangement.entity.dto;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

//课程数据导出导入
@Data
public class CourseInfoDTO {
    @ExcelProperty("开课学院")
    private String college;
    @ExcelProperty("课程属性")
    private String courseAttribute;
    @ExcelProperty("课程编号")
    private String courseNo;
    @ExcelProperty("课程名")
    private String courseName;
    @ExcelProperty("上课教师")
    private String teacherName;
    @ExcelProperty("教室类型")
    private String classroomType;
}
