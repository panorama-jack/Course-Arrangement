package com.chen.coursearrangement.mapper;

import com.chen.coursearrangement.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Jack Chen
 * @since 2023-05-17
 */
public interface RoleMapper extends BaseMapper<Role> {
    //查询用户角色
    @Select("select id from sys_role where flag=#{flag}")
    Integer selectByFlag(@Param("flag") String role);
}
