package com.zhangrun.service.impl;

import cn.itcast.commons.CommonUtils;
import com.zhangrun.dao.impl.UserDao;
import com.zhangrun.entity.User;
import com.zhangrun.exception.UserException;
import com.zhangrun.service.IUserService;
import com.zhangrun.utils.MailUtils;
import java.sql.SQLException;


/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/14 12:32
 */
public class UserService implements IUserService {
    private UserDao userDao=new UserDao();
    /*
     * @Param [loginname]
     * @return boolean
     * 校验用户名是否存在
    */
    @Override
    public boolean validateLoginname(String loginname) {
        try {
            return userDao.findByName(loginname);
        } catch (SQLException e) {
            throw new RuntimeException (e);
        }
    }
    /*
     * @Param
     *校验email是否存在
     * @return boolean
    */
    @Override
    public boolean validateEmail(String email) {
        try {
            return userDao.findByEmail(email);
        } catch (SQLException e) {
            throw new RuntimeException (e);
        }
    }
    //用户注册
    @Override
    public void regist(User user) {
        user.setUid(CommonUtils.uuid());
        user.setStatus(false);
        user.setActivationCode(CommonUtils.uuid()+CommonUtils.uuid());
        userDao.add(user); //新增
        String content="<a href='http://localhost:8080/goods/userServlet?method=activation&activationCode="+user.getActivationCode()+"'>网上商城</a>";
        MailUtils.sendMail(user.getEmail(),content,"激活邮件");
        /*Properties properties=new Properties();  //根据类加载器读取配置文件到properties中
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        *//*
         *登录邮件服务器，得到session
        *//*
        String host=properties.getProperty("host");     //服务器主机名
        String name=properties.getProperty("username");     //登录名
        String pass=properties.getProperty("password");     //登录密码
        Session session= cn.itcast.mail.MailUtils.createSession(host,name,pass);

        *//*
         *创建mail 对象
        *//*
        String from=properties.getProperty("from");
        String to=user.getEmail();
        String subject=properties.getProperty("subject");
        //MessageFormat.format()方法会把第一个参数中的{0}使用第二个参数来替换

        String content= MessageFormat.format(properties.getProperty("content"),user.getActivationCode()); //把配置文件中{0}替换成user.getActivationCode()
        Mail mail=new Mail(from,to,subject,content);
        //发邮件
        try {
            MailUtils.send(session,mail);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

    }

    @Override
    public void activation(String activationCode) throws UserException {
        /*
         * 1. 通过激活码查询用户
         * 2. 如果User为null，说明是无效激活码，抛出异常，给出异常信息（无效激活码）
         * 3. 查看用户状态是否为true，如果为true，抛出异常，给出异常信息（请不要二次激活）
         * 4. 修改用户状态为true
         */
        User user = userDao.findByCode(activationCode);
        if(user == null) throw new UserException("无效的激活码！");
        if(user.isStatus()) throw new UserException("您已经激活过了，不要二次激活！");
        userDao.updataStatus(user.getUid(), true);//修改状态
    }


    /*
         * @Param [user]
         * @return com.zhangrun.entity.User
         * 用户登录功能
        */
    @Override
    public User login(User user) {
        return userDao.login(user.getLoginname(),user.getLoginpass());
    }
    /*
     * @Param [uid, newPass, oldPass]
     * @return void
     * 修改密码功能
    */
    @Override
    public void updatePass(String uid, String newPass, String oldPass) throws UserException {
        try {
            boolean bool= false;
            bool = userDao.findByuidAndPass(uid, oldPass);
            if (!bool){
                throw new UserException("旧密码错误");
            }
            userDao.updatePass(uid,newPass);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /*@Override
    public void activation(String activationCode)throws UserException {
        *//*
         * 1. 通过激活码查询用户
         * 2. 如果User为null，说明是无效激活码，抛出异常，给出异常信息（无效激活码）
         * 3. 查看用户状态是否为true，如果为true，抛出异常，给出异常信息（请不要二次激活）
         * 4. 修改用户状态为true
         *//*
        User byCode = userDao.findByCode(activationCode);
        if(byCode == null){
            throw new UserException("无效的激活码！");
        }
        if(byCode.isStatus()){
            throw new UserException("您已经激活过了，不要二次激活！");
        }
        userDao.updataStatus(byCode.getUid(), true);//修改状态
    }*/

}
