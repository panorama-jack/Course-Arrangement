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
 * @since 2024-01-15
 */
@Getter
@Setter
@ApiModel(value = "Teacher对象", description = "")
public class Teacher implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("学院编号")
    private String collegeNo;

    @ApiModelProperty("工号")
    private String teacherNo;

    @ApiModelProperty("姓名")
    private String teacherName;

    @ApiModelProperty("职称")
    private String profession;

    @ApiModelProperty("电话")
    private String telephone;

}
