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
 * @since 2023-05-20
 */
@Getter
@Setter
@ApiModel(value = "Notification对象", description = "")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("发布人")
    private String user;

    @ApiModelProperty("发布时间")
    private String time;

    @ApiModelProperty("学院编号")
    private String collegeNo;


    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("内容")
    private String content;



}
