package com.zhangrun.dao.impl;

import cn.itcast.jdbc.TxQueryRunner;
import com.zhangrun.dao.IUserDao;
import com.zhangrun.entity.User;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/14 11:10
 */
public class UserDao implements IUserDao{
    private TxQueryRunner txQueryRunner=new TxQueryRunner();
    //校验用户名是否存在
    @Override
    public boolean findByName(String loginname)throws SQLException{
        String sql="select count(1) from t_user where loginname = ?";
        Number number=(Number) txQueryRunner.query(sql,new ScalarHandler(),loginname);//单行单列用scalarHandler
        return number.intValue()==0;
    }
    //校验email是否存在
    @Override
    public boolean findByEmail(String email)throws SQLException {
        String sql="select count(1) from t_user where email = ?";
        Number number=(Number) txQueryRunner.query(sql,new ScalarHandler(),email);//单行单列
        return number.intValue()==0;
    }
    //用户新增
    @Override
    public void add(User user) {
        String sql="insert into t_user values(?,?,?,?,?,?)";
        Object [] params={user.getUid(),user.getLoginname(),user.getLoginpass(),
            user.getEmail(),user.isStatus(),user.getActivationCode()
        };
        try {
            txQueryRunner.update(sql,params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //根据激活码查询用户
    @Override
    public User findByCode(String activationCode) {
        String sql="select * from t_user where activationCode = ?";
        User user=null;
        try {
            user=txQueryRunner.query(sql,new BeanHandler<User>(User.class),activationCode);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    //修改用户状态
    @Override
    public void updataStatus(String uid, boolean status) {
        String sql="update t_user set status = ? where uid = ?";
        try {
            txQueryRunner.update(sql,status,uid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /*
     * @Param [loginname, loginpass]
     * @return com.zhangrun.entity.User
     * 用户登录功能
    */
    @Override
    public User login(String loginname, String loginpass) {
        String sql="select * from t_user where loginname = ? and loginpass = ?";
        User user=null;
        try {
            user=txQueryRunner.query(sql,new BeanHandler<User>(User.class),loginname,loginpass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    /*
     * @Param [uid, oldPass]
     * @return boolean
     * 根据uid 和修密码查询用户
    */
    @Override
    public boolean findByuidAndPass(String uid, String oldPass) throws SQLException {
        String sql="select count(*) from t_user where uid = ? and loginpass = ?";
        Number number=(Number) txQueryRunner.query(sql,new ScalarHandler(),uid,oldPass);
        return number.intValue()>0;  //查询的结果大于0 就返回true 证明该用户的原密码正确
    }
    /*
     * @Param [uid, newPass]
     * @return void
     * 根据uid来修改密码
    */
    @Override
    public void updatePass(String uid, String newPass) throws SQLException {
        String sql="update t_user set loginpass = ? where uid = ?";
        txQueryRunner.update(sql,newPass,uid);
    }
}
