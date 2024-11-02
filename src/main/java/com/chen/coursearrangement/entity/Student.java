package com.chen.coursearrangement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2023-05-12
 */
@Getter
@Setter
  @ApiModel(value = "Student对象", description = "")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("id")
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("年级编号")
      private String gradeNo;

      @ApiModelProperty("学院编号")
      private String collegeNo;

      @ApiModelProperty("班级编号")
      private String classNo;

      @ApiModelProperty("学号")
      private String studentNo;

      @ApiModelProperty("姓名")
      private String studentName;

      @ApiModelProperty("电话")
      private String telephone;

}
