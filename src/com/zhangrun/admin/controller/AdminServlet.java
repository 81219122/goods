package com.zhangrun.admin.controller;

import cn.itcast.servlet.BaseServlet;
import com.sun.org.apache.regexp.internal.RE;
import com.zhangrun.admin.entity.Admin;
import com.zhangrun.admin.service.impl.AdminServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/29 15:04
 * 管理员servlet
 */
@WebServlet("/adminServlet")
public class AdminServlet extends BaseServlet {
    private AdminServiceImpl adminService=new AdminServiceImpl();
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String adminname = request.getParameter("adminname");
        String adminpwd = request.getParameter("adminpwd");
        Admin admin = adminService.login(adminname, adminpwd);
        if (admin == null){
            request.setAttribute("msg","用户名或密码错误");
            request.setAttribute("adminname",adminname);
            request.setAttribute("adminpwd",adminpwd);
            request.getRequestDispatcher("/adminjsps/login.jsp").forward(request,response);
        }else {
            request.getSession().setAttribute("admin",admin);
            request.getRequestDispatcher("/adminjsps/admin/index.jsp").forward(request,response);
        }

    }

    /*
     * @Param [request, response]
     * @return void
     * 管理员退出功能
    */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().invalidate();//销毁session
        response.sendRedirect(request.getContextPath()+"/adminjsps/login.jsp");
    }
}
