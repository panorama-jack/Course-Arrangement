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
 * @since 2024-01-15
 */
@Getter
@Setter
@TableName("college_info")
@ApiModel(value = "CollegeInfo对象", description = "")
public class CollegeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("id")
        @TableId(value = "id", type = IdType.AUTO)
      private Long id;

      @ApiModelProperty("学院编号")
      private String collegeNo;

      @ApiModelProperty("学院名称")
      private String collegeName;
}
