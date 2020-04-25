package com.zhangrun.admin.dao;

import com.zhangrun.admin.entity.Admin;

import java.sql.SQLException;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/29 15:04
 */
public interface IAdminDao {
    //管理员登录功能
    Admin login(String adminname,String adminpwd) throws SQLException;
}
