package com.zhangrun.admin.service;

import com.zhangrun.admin.entity.Admin;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/29 15:04
 */
public interface IAdminService {
    //管理员登录功能
    Admin login(String adminname, String adminpwd);
}
