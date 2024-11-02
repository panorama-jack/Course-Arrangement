package com.chen.coursearrangement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Jack Chen
 * @since 2024-01-19
 */
@Getter
@Setter
@TableName("course_plan")
@ApiModel(value = "CoursePlan对象", description = "")
public class CoursePlan implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("id")
      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("学期")
      private String term;

      @ApiModelProperty("学院")
      private String collegeNo;

      @ApiModelProperty("年级编号")
      private String gradeNo;

      @ApiModelProperty("班级编号")
      private String classNo;

      @ApiModelProperty("课程编号")
      private String courseNo;

      @ApiModelProperty("周学时")
      private Integer weekTime;

      @ApiModelProperty("周数")
      private Integer weeks;

      @ApiModelProperty("上课时间是否固定")
      private String isFix;

      @ApiModelProperty("上课时间")
      private String courseTime;


}
