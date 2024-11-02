package com.chen.coursearrangement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("course_info")
@ApiModel(value = "CourseInfo对象", description = "")
public class CourseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("id")
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("开课学院")
      private String collegeNo;

      @ApiModelProperty("课程编号")
      private String courseNo;

      @ApiModelProperty("课程名称")
      private String courseName;

      @ApiModelProperty("授课教师工号")
      private String teacherNo;

      @ApiModelProperty("课程属性")
      private String courseAttribute;

      @ApiModelProperty("教室类型")
      private String classroomType;

      @TableField(exist = false)
      private Teacher teacher;

}
