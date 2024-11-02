package com.chen.coursearrangement.entity.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
//学生数据查询，导出导入
@Data
public class StudentDTO {
    /**
     * 忽略这个字段
     */
    @ExcelIgnore
    private Integer id;
    @ExcelProperty("年级")
    private String grade;
    @ExcelProperty("学院")
    private String college;
    @ExcelProperty("班级")
    private String className;
    @ExcelProperty("学号")
    private String studentNo;
    @ExcelProperty("姓名")
    private String studentName;
    @ExcelProperty("电话")
    private String telephone;

}
