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
 * @since 2024-01-12
 */
@Getter
@Setter
@ApiModel(value = "Schedule对象", description = "")
public class Schedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("学期")
    private String term;

    @ApiModelProperty("年级编号")
    private String gradeNo;

    @ApiModelProperty("班级编号")
    private String classNo;

    @ApiModelProperty("课程编号")
    private String courseNo;

    @ApiModelProperty("教师编号")
    private String teacherNo;

    @ApiModelProperty("教室编号")
    private String classroomNo;

    @ApiModelProperty("上课时间")
    private String courseTime;

}
