package com.chen.coursearrangement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.coursearrangement.entity.User;
import com.chen.coursearrangement.entity.dto.UserDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jack Chen
 * @since 2023-05-15
 */
public interface IUserService extends IService<User> {

    User login(User user);

    void updatePassword(User user);

    String getUserCollege(String userNo);

    int editUserRole(Integer id, String role);

    int resetPassword(String userNo);

    UserDTO getUserRoleAndCollege(String userNo);

    String getUserTelephone(String userNo);

    int editStudentTelephone(String userNo, String telephone);

    int editTeacherTelephone(String userNo, String telephone);
}
