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
 * @since 2023-05-17
 */
@Getter
@Setter
@TableName("sys_file")
@ApiModel(value = "File对象", description = "")
public class Files implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty("文件名")
    private String name;
    @ApiModelProperty("类型")
    private String type;
    @ApiModelProperty("大小")
    private Long size;
    @ApiModelProperty("文件位置")
    private String url;
    @ApiModelProperty("是否删除")
    private Boolean isDelete;
    @ApiModelProperty("md5")
    private String md5;


}
