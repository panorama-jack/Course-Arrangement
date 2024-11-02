package com.chen.coursearrangement.service;

import com.chen.coursearrangement.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jack Chen
 * @since 2023-05-17
 */
public interface IRoleService extends IService<Role> {

    void saveRoleMenu(Integer roleId, List<Integer> menuIds);

    List <Integer> getRoleMenu(Integer roleId);
}
