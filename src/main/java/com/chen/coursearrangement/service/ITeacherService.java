package com.chen.coursearrangement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.coursearrangement.entity.Teacher;
import com.chen.coursearrangement.entity.dto.TeacherDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jack Chen
 * @since 2024-01-15
 */
public interface ITeacherService extends IService<Teacher> {

    Page<Teacher> findPage(Page<Teacher> page, String teacherNo, String teacherName, String college);

    int getMaxLastThreeDigits(String college);

    List<TeacherDTO> export(String college, String teacherNo, String teacherName);

    int imp(List<TeacherDTO> list);

    int addTeacherUser(Teacher teacher);

    int deleteTeacherUser(Integer id);
}
