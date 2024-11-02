package com.chen.coursearrangement.service;

import com.chen.coursearrangement.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jack Chen
 * @since 2023-05-16
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> findMenus(String name);
}
