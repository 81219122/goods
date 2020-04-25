package com.zhangrun.admin.entity;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/29 15:01
 * 管理员实体类
 */
public class Admin {
    private String adminId;  //主键
    private String adminname;  //管理员用户名
    private String adminpwd; //管理员密码

    @Override
    public String toString() {
        return "Admin{" + "adminId='" + adminId + '\'' + ", adminname='" + adminname + '\'' + ", adminpwd='" + adminpwd + '\'' + '}';
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }

    public String getAdminpwd() {
        return adminpwd;
    }

    public void setAdminpwd(String adminpwd) {
        this.adminpwd = adminpwd;
    }
}
