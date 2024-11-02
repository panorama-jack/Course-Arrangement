package com.chen.coursearrangement.service.impl;
import com.chen.coursearrangement.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
//@Transactional
class UserServiceImplTest {
    @Resource
    private UserServiceImpl userServiceImpl;
    @Test
    void login() {
        User user=new User();
        user.setUsername("22001");
        user.setPassword("922001");
        User login=userServiceImpl.login(user);
        Assertions.assertEquals(user.getUsername(),login.getUsername());
    }
    //通过旧密码修改新密码
    @Test
    //@Rollback
    void updatePassword() {
        User user=new User("admin","admin123","admin11");
        //先修改密码，再修改上面对象的密码字段，然后再调用getUserInfo到数据库进行查询
        userServiceImpl.updatePassword(user);
        user.setPassword("admin11");
        Assertions.assertEquals("admin11",userServiceImpl.getUserInfo(user).getPassword());
    }
}