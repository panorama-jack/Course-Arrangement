package com.chen.coursearrangement.service.impl;

import com.chen.coursearrangement.entity.Files;
import com.chen.coursearrangement.mapper.FileMapper;
import com.chen.coursearrangement.service.IFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jack Chen
 * @since 2023-05-17
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, Files> implements IFileService {

}
