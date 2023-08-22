package com.diao.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.diao.usercenter.model.User;

import javax.servlet.http.HttpServletRequest;


/**
* @author cairang
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2023-08-21 16:18:55
*/
public interface UserService extends IService<User> {

    /**
     *
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     *
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @return
     */
    long doLogin(String userAccount, String userPassword, HttpServletRequest request);
}
