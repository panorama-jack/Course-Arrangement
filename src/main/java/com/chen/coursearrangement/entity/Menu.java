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
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author Jack Chen
 * @since 2023-05-16
 */
@Getter
@Setter
@TableName("sys_menu")
@ApiModel(value = "Menu对象", description = "")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("路径")
    private String path;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("顺序")
    private Integer sortNum;

    @ApiModelProperty("父级id")
    private Integer pid;

    @ApiModelProperty("页面路径")
    private String pagePath;

    @TableField(exist = false)
    private List<Menu> children;

}
