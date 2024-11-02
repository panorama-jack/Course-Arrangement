package com.chen.coursearrangement.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.chen.coursearrangement.entity.Menu;
import com.chen.coursearrangement.entity.Role;
import com.chen.coursearrangement.entity.RoleMenu;
import com.chen.coursearrangement.mapper.RoleMapper;
import com.chen.coursearrangement.mapper.RoleMenuMapper;
import com.chen.coursearrangement.service.IMenuService;
import com.chen.coursearrangement.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Jack Chen
 * @since 2023-05-17
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    private RoleMenuMapper roleMenuMapper;


    @Resource
    private IMenuService menuService;

    //保证同时成功与失败。如果发生错误，回滚
    @Transactional
    @Override
    public void saveRoleMenu(Integer roleId, List<Integer> menuIds) {
      /*  QueryWrapper<RoleMenu> menuQueryWrapper = new QueryWrapper<>();
        menuQueryWrapper.eq("role_id",roleId);
        roleMenuMapper.delete(menuQueryWrapper);*/

        //先删除当前角色id所有绑定关系
        roleMenuMapper.deleteByRoleId(roleId);

        // 再把前端传过来的菜单id数组绑定到当前的这个角色id上去
        List<Integer> menuIdsCopy = CollUtil.newArrayList(menuIds);
        for (Integer menuId : menuIds) {
            Menu menu = menuService.getById(menuId);
            if (menu.getPid() != null && !menuIdsCopy.contains(menu.getPid())) { // 二级菜单 并且传过来的menuId数组里面没有它的父级id
                // 那么我们就得补上这个父级id
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menu.getPid());
                roleMenuMapper.insert(roleMenu);
                menuIdsCopy.add(menu.getPid());
            }
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuMapper.insert(roleMenu);
        }
    }

    @Override
    public List<Integer> getRoleMenu(Integer roleId) {
        return roleMenuMapper.selectByRoleId(roleId);
    }
}