package com.zhangrun.admin.dao.impl;

import cn.itcast.jdbc.TxQueryRunner;
import com.zhangrun.admin.dao.IAdminDao;
import com.zhangrun.admin.entity.Admin;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.SQLException;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/29 15:07
 */
public class AdminDaoImpl implements IAdminDao {
    private TxQueryRunner txQueryRunner=new TxQueryRunner();

    /*
     * @Param [adminname, adminpwd]
     * @return com.zhangrun.admin.entity.Admin
     * 管理员登录功能
    */
    @Override
    public Admin login(String adminname, String adminpwd) throws SQLException {
        String sql="select * from t_admin where adminname = ? and adminpwd = ?";
        Admin admin = txQueryRunner.query(sql, new BeanHandler<Admin>(Admin.class), adminname, adminpwd);
        return admin;
    }
}
