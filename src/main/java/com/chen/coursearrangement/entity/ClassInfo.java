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
 * @since 2024-01-17
 */
@Getter
@Setter
@TableName("class_info")
@ApiModel(value = "ClassInfo对象", description = "")
public class ClassInfo implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("id")
      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("学院编号")
      private String collegeNo;

      @ApiModelProperty("年级编号")
      private String gradeNo;

      @ApiModelProperty("专业编号")
      private String majorNo;

      @ApiModelProperty("班级编号")
      private String classNo;

      @ApiModelProperty("班级名称")
      private String className;

      @ApiModelProperty("班主任工号")
      private String teacherNo;

      @ApiModelProperty("班级人数")
      private Integer studentNumber;
}
