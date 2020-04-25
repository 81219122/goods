package com.zhangrun.admin.service.impl;

import com.zhangrun.admin.dao.impl.AdminDaoImpl;
import com.zhangrun.admin.entity.Admin;
import com.zhangrun.admin.service.IAdminService;

import java.sql.SQLException;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/29 15:08
 */
public class AdminServiceImpl implements IAdminService {
    private AdminDaoImpl adminDao=new AdminDaoImpl();

    /*
     * @Param [adminname, adminpwd]
     * @return com.zhangrun.admin.entity.Admin
     * 管理员登录功能
    */
    @Override
    public Admin login(String adminname, String adminpwd) {
        try {
            return adminDao.login(adminname,adminpwd);
        } catch (SQLException e) {
           throw new RuntimeException(e);
        }
    }
}
