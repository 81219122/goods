package com.zhangrun.dao;

import com.zhangrun.entity.User;

import java.sql.SQLException;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/14 10:51
 */
public interface IUserDao {
    //根据用户名查询对象
    boolean findByName(String loginname) throws SQLException;

    //校验email是否注册
    boolean findByEmail(String email) throws SQLException;

    //添加用户
    void add(User user);

    //根据激活码查询对象
    User findByCode(String activationCode);

    //修改用户状态
    void updataStatus(String uid,boolean status);

    //登录功能
    User login(String loginname,String loginpass);

    //根据uid和旧密码查询对象
    boolean findByuidAndPass(String uid,String oldPass) throws SQLException;

    //根据uid修改密码
    void updatePass(String uid,String newPass) throws SQLException;
}
