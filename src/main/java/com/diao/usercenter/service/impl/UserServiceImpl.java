package com.diao.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.diao.usercenter.mapper.UserMapper;
import com.diao.usercenter.model.User;
import com.diao.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author cairang
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2023-08-21 16:18:55
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 盐值：密码加密
     */
    private static final String SALT = "diao";
    /**
     * 用户登录状态
     */
    private static final String USER_LOGIN_STAGE = "userLoginState";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
         if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
             return -1;
         }
         if (userAccount.length() < 4){
             return -1;
         }
         if (userPassword.length() < 8 || checkPassword.length() < 8){
             return -1;
         }
         //判断账户是否包含特殊字符
        // 定义一个正则表达式，匹配除了字母和数字之外的字符
        String regex = "[^a-zA-Z0-9]";
        Matcher matcher = Pattern.compile(regex).matcher(userAccount);
        if (matcher.find()){
            return -1;
        }
        // 检查两次输入的密码是否一致
        if(!userPassword.equals(checkPassword)){
            return -1;
        }
        // 账户不能重复校验
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0){
            return -1;
        }
        // 2、加密
        String md5Password = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 3、插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(md5Password);
        int insert = userMapper.insert(user);
        System.out.println(insert);
        return user.getId();
    }

    @Override
    public long doLogin(String userAccount, String userPassword, HttpServletRequest request) {
        if (StringUtils.isAnyBlank(userAccount, userPassword)){
            log.error("Login failed, incorrect account or password!");
            return -1;
        }
        // 2、加密
        String md5Password = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword", md5Password);
        boolean exists = userMapper.exists(queryWrapper);
        if (!exists){
            log.error("Login failed, incorrect password!");
            return -1;
        }
        // 3、 record user status
        User safetyUser = new User();
        safetyUser.setUserAccount(userAccount);
        safetyUser.setUserPassword(userPassword);
        // 4、record user login status
        request.getSession().setAttribute(USER_LOGIN_STAGE,safetyUser);
        log.info("Login success ~");
        return 1;
    }
}




