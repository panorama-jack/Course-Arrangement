package com.chen.coursearrangement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.coursearrangement.entity.User;
import com.chen.coursearrangement.entity.dto.UserDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Jack Chen
 * @since 2023-05-15
 */
public interface UserMapper extends BaseMapper<User> {
    //修改用户密码
    @Update("update user set password = #{newPassword} where user_no = #{userNo} and password = #{password}")
    int updatePassword(User user);
    //获取用户学院信息
    @Select("SELECT college_no FROM `teacher` WHERE teacher_no=#{userNo}")
    String getUserCollege(@Param("userNo")String userNo);
    //修改用户角色
    @Update("update user set role = #{role} where id = #{id}")
    int editUserRole(@Param("id")Integer id, @Param("role")String role);
    @Update("update user set password = #{password} where user_no = #{userNo}")
    //重置用户密码
    int resetPassword(@Param("userNo")String userNo, @Param("password")String password);
    //查询用户的学院和角色信息
    UserDTO getUserRoleAndCollege(@Param("userNo")String userNo);
    //删除用户信息
    @Delete("delete from user where user_no=#{userNo}")
    int removeByUserNo(@Param("userNo")String userNo);
    //获取用户电话号码
    String getUserTelephone(@Param("userNo")String userNo);
    @Update(" UPDATE student SET telephone = #{telephone} WHERE student_no = #{userNo}")
    //学生修改电话号码
    int editStudentTelephone(@Param("userNo")String userNo,@Param("telephone") String telephone);
    //教师修改电话号码
    @Update(" UPDATE teacher SET telephone = #{telephone} WHERE teacher_no = #{userNo}")
    int editTeacherTelephone(@Param("userNo")String userNo,@Param("telephone") String telephone);
    //用户角色查询     sa_token使用
    @Select("SELECT role FROM user WHERE user_no=#{userNo}")
    String getUserRole(@Param("userNo")String userNo);
}
