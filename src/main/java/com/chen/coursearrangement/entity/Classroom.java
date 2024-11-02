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
 * @since 2023-05-19
 */
@Getter
@Setter
@ApiModel(value = "Classroom对象", description = "")
public class Classroom implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("教室编号")
      private String classroomNo;

      @ApiModelProperty("教学楼编号")
      private String teachingBuildingNo;

      @ApiModelProperty("教室名称")
      private String classroomName;

      @ApiModelProperty("教室容量")
      private Integer capacity;

      @ApiModelProperty("教室类型")
      private String classroomType;
}
