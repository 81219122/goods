package com.zhangrun.entity;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/13 11:12
 * 用户实体类
 */
public class User {
    private String uid;//主键
    private String loginname;//登录账户名
    private String loginpass;//登录密码
    private String email;//邮箱
    private boolean status;//状态 1激活 0未激活
    private String activationCode;//激活码，唯一值 每个用户都不一样

    private String reloginpass;//确认密码
    private String verifyCode;//验证码；

    private String newpass;//新密码

    @Override
    public String toString() {
        return "User{" + "uid='" + uid + '\'' + ", loginname='" + loginname + '\'' + ", loginpass='" + loginpass + '\'' + ", email='" + email + '\'' + ", status=" + status + ", activationCode='" + activationCode + '\'' + ", reloginpass='" + reloginpass + '\'' + ", verifyCode='" + verifyCode + '\'' + ", newpass='" + newpass + '\'' + '}';
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getLoginpass() {
        return loginpass;
    }

    public void setLoginpass(String loginpass) {
        this.loginpass = loginpass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getReloginpass() {
        return reloginpass;
    }

    public void setReloginpass(String reloginpass) {
        this.reloginpass = reloginpass;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getNewpass() {
        return newpass;
    }

    public void setNewpass(String newpass) {
        this.newpass = newpass;
    }
}
