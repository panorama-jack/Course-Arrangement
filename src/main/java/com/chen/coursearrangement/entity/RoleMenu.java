package com.chen.coursearrangement.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

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
@TableName("sys_role_menu")
@ApiModel(value = "RoleMenu对象", description = "")
public class RoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("角色id")
    private Integer roleId;

    @ApiModelProperty("菜单id")
    private Integer menuId;

}
