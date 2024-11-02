package com.chen.coursearrangement.service;

import com.chen.coursearrangement.entity.ClassInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jack Chen
 * @since 2024-01-17
 */
public interface IClassInfoService extends IService<ClassInfo> {

    int lastTwoDigits(String gradeNo, String majorNo);

}
