package com.diao.usercenter.service;
import java.util.Date;

import com.diao.usercenter.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@SpringBootTest
public class UserServiceTest {
    @Resource
    private UserService userService;

    @Test
    void testAddUser(){
        User user = new User();
//        user.setId(0L);
        user.setUsername("zhangsan");
        user.setUserAccount("zhangsan");
        user.setAvatarUrl("san");
        user.setGender(0);
        user.setUserPassword("123456");
        user.setPhone("123456");
        user.setEmail("123@456.com");
        user.setUserStatus(0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setIdDelete(0);


        boolean save = userService.save(user);
        System.out.println(user.getId());
    }

    @Test
    void userRegister() {
        String userAccount= "wangwu";
        String userPassword = "123456789";
        String chechPassword = "123456789";
        long l = userService.userRegister(userAccount, userPassword, chechPassword);
        System.out.println(l);
    }

    @Test
    void doLogin() {
        String userAccount= "wangwu";
        String userPassword = "123456789";
        long l = userService.doLogin(userAccount, userPassword);
        System.out.println(l);
    }
}