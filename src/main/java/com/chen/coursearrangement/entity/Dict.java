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
 * @since 2024-01-19
 */
@Getter
@Setter
@TableName("sys_dict")
@ApiModel(value = "Dict对象", description = "")
public class Dict implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("ID")
      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("名称")
      private String name;

      @ApiModelProperty("值")
      private String value;

      @ApiModelProperty("类型")
      private String type;


}
