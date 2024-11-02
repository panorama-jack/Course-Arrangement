package com.chen.coursearrangement.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.coursearrangement.common.ConstantInfo;
import com.chen.coursearrangement.entity.Menu;
import com.chen.coursearrangement.entity.User;
import com.chen.coursearrangement.entity.dto.UserDTO;
import com.chen.coursearrangement.exception.ServiceException;
import com.chen.coursearrangement.mapper.RoleMapper;
import com.chen.coursearrangement.mapper.RoleMenuMapper;
import com.chen.coursearrangement.mapper.UserMapper;
import com.chen.coursearrangement.service.IMenuService;
import com.chen.coursearrangement.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Jack Chen
 * @since 2023-05-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private static final Log LOG = Log.get();
    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;


    @Resource
    private IMenuService menuService;

    @Override
    public User login(User user) {
        User one = getUserInfo(user);
        if (one != null) {//登录成功
            //sa_token设置token
            StpUtil.login(user.getUserNo());
            String token = StpUtil.getTokenInfo().tokenValue;
            one.setToken(token);
            String role = one.getRole();  //ROLE_ADMIN
            //设置用户菜单列表
            List<Menu> roleMenus = getRoleMenus(role);
            one.setMenus(roleMenus);
            return one;
        } else {
            throw new ServiceException(ConstantInfo.CODE_600, "用户名或密码错误");
        }
    }

    @Override
    public void updatePassword(User user) {
        //AES先进行加密操作
        String oldCiphertext = SaSecureUtil.aesEncrypt(user.getUserNo()+user.getPassword(), user.getPassword());
        String newCiphertext = SaSecureUtil.aesEncrypt(user.getUserNo()+user.getNewPassword(), user.getNewPassword());
        user.setPassword(oldCiphertext);   //旧密码
        user.setNewPassword(newCiphertext);  //新密码
        int update = userMapper.updatePassword(user);
        if (update < 1) {
            throw new ServiceException(ConstantInfo.CODE_600, "密码错误");
        }
    }
    @Override
    public String getUserCollege(String userNo) {
        return userMapper.getUserCollege(userNo);
    }

    @Override
    public int editUserRole(Integer id, String role) {
        return userMapper.editUserRole(id, role);
    }

    //重置密码
    @Override
    public int resetPassword(String userNo) {
        String password = "";
        User user = userMapper.selectById(userNo);
        if (user.getRole().equals("ROLE_STUDENT")) {
             password = user.getUserNo().substring(user.getUserNo().length() - 6);   //学号后六位
        } else {
             password = "9" + user.getUserNo();                 //9+工号
        }
        String ciphertext = SaSecureUtil.aesEncrypt(user.getUserNo()+password, password);
        return userMapper.resetPassword(userNo, ciphertext);
    }

    @Override
    public UserDTO getUserRoleAndCollege(String userNo) {
        return userMapper.getUserRoleAndCollege(userNo);
    }

    @Override
    public String getUserTelephone(String userNo) {
        return userMapper.getUserTelephone(userNo);
    }

    @Override
    public int editStudentTelephone(String userNo, String telephone) {
        return userMapper.editStudentTelephone(userNo,telephone);
    }

    @Override
    public int editTeacherTelephone(String userNo, String telephone) {
        return userMapper.editTeacherTelephone(userNo,telephone);
    }

    //此处此修改过，原为private
    public User getUserInfo(User user) {  //登录判断
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 加密  使用用户账号加密码作为key    AES加密
        String ciphertext = SaSecureUtil.aesEncrypt(user.getUserNo()+user.getPassword(), user.getPassword());
        System.out.println("AES加密后：" + ciphertext);

        queryWrapper.eq("user_no", user.getUserNo());
        queryWrapper.eq("password", ciphertext);
        User one;
        try {
            one = getOne(queryWrapper); // 从数据库查询用户信息
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(ConstantInfo.CODE_500, "系统错误");
        }
        return one;
    }

    /**
     * 获取当前角色的菜单列表
     *
     * @param roleFlag
     * @return
     */
    private List<Menu> getRoleMenus(String roleFlag) {
        Integer roleId = roleMapper.selectByFlag(roleFlag);
        //当前角色的所有菜单id集合
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);
        //查出系统所有的菜单
        List<Menu> menus = menuService.findMenus("");
        //new一个最后筛选完成之后的list
        List<Menu> roleMenus = new ArrayList<>();
        //筛选当前用户角色的菜单
        for (Menu menu : menus) {
            //包含就往里面丢
            if (menuIds.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            //移除children里面不在menuIds集合中的元素
            children.removeIf(child -> !menuIds.contains((child.getId())));
        }
        return roleMenus;
    }
}
