package com.chen.coursearrangement.mapper;

import com.chen.coursearrangement.entity.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Jack Chen
 * @since 2023-05-17
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    //查询该用户对应的菜单id列表
    @Select("select menu_id from sys_role_menu where role_id= #{roleId}")
    List<Integer> selectByRoleId(@Param("roleId") Integer roleId);
    //删除对应角色的角色-菜单数据
    @Delete("delete from sys_role_menu where role_id= #{roleId}")
    void deleteByRoleId(@Param("roleId") Integer roleId);
}
