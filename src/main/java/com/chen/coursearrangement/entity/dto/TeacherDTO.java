package com.chen.coursearrangement.entity.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

//教师数据查询，导出导入
@Data
public class TeacherDTO {
    @ExcelIgnore
    private Integer id;
    @ExcelProperty("学院")
    private String college;
    @ExcelProperty("工号")
    private String teacherNo;
    @ExcelProperty("姓名")
    private String teacherName;
    @ExcelProperty("职称")
    private String profession;
    @ExcelProperty("电话")
    private String telephone;
}
