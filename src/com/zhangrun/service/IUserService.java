package com.zhangrun.service;

import com.zhangrun.entity.User;
import com.zhangrun.exception.UserException;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/14 10:52
 */
public interface IUserService {
    //用户名校验
    boolean validateLoginname(String loginname);
    //邮箱校验
    boolean validateEmail(String email);
    //注册功能
    void regist(User user);

    //用户激活
    void activation(String activationCode) throws UserException;

    //用户登录
    User login(User user);

    //修改密码
    void updatePass(String uid,String newPass,String oldPass) throws UserException;
}
