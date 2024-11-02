package com.chen.coursearrangement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
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
 * @since 2023-05-15
 */
@Getter
@Setter
@ApiModel(value = "User对象", description = "")
public class User implements Serializable {

    //构造方法
    public User() {
    }

    public User(String userNo, String password, String newPassword) {
        this.userNo = userNo;
        this.password = password;
        this.newPassword = newPassword;
    }

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户账号")
    private String userNo;

    @ApiModelProperty("用户密码")
    /**
     * 密码
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ApiModelProperty("用户角色")
    private String role;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户头像")
    private String avatarUrl;

    @ApiModelProperty("个性签名")
    private String signature;


    @TableField(exist = false)
    private String token;

    @TableField(exist = false)
    private String telephone;

    @TableField(exist = false)
    private List<Menu> menus;

    @TableField(exist = false)
    private String newPassword;

}
