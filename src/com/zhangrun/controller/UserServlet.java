package com.zhangrun.controller;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import com.zhangrun.entity.User;
import com.zhangrun.exception.UserException;
import com.zhangrun.service.impl.UserService;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/14 10:38
 */
@WebServlet("/userServlet")
public class UserServlet extends BaseServlet {
    private UserService userService=new UserService();
    //注册功能
    public String ajaxValidateLoginname(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String loginname = request.getParameter("loginname");//获取页面的用户名
        boolean b = userService.validateLoginname(loginname);//通过service得到校验结果
        response.getWriter().print(b);  //发回给客户端
        return null;
    }

    //校验邮箱
    public String ajaxValidateEmail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");//获取页面的用户名
        boolean b = userService.validateEmail(email);
        response.getWriter().print(b);
        return null;
    }

    //校验验证码
    public String ajaxValidateVerifyCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String verifycode = request.getParameter("verifyCode");//获取页面的用户名
        String vCode = (String) request.getSession().getAttribute("vCode");
        boolean b=verifycode.equalsIgnoreCase(vCode);  //忽略大小写校验验证码
        response.getWriter().print(b);
        return null;
    }
    //用户注册功能
    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.封装表单数据到user对象
        User userForm= CommonUtils.toBean(request.getParameterMap(),User.class);
        //2.校验，如果失败返回错误信息，返回到regist.jsp页面
        Map<String, String> errors  = validateRegist(userForm, request.getSession());
        if (errors .size()>0){
            request.setAttribute("errors",errors );
            request.setAttribute("form",userForm);   //表单注册失败，把信息带回input中 避免输入一项错误就清空了表单
            //return "f:/jsps/user/regist.jsp";
            request.getRequestDispatcher("/jsps/user/regist.jsp").forward(request,response);
        }else {
            //3.使用service完成业务
            userService.regist(userForm);
            //4.保存成功信息，转发到msg.jsp页面
            request.setAttribute("code","sussess");
            request.setAttribute("msg","注册成功，请到邮箱激活");
            request.getRequestDispatcher("/jsps/msg.jsp").forward(request,response);
            //return "d:/jsps/msg.jsp";  //必须是转发，因为request中放了东西
        }


    }

    //用户激活功能
    public void activation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("activationCode");
        try {
            userService.activation(code);
            request.setAttribute("code", "success");//通知msg.jsp显示对号
            request.setAttribute("msg", "恭喜，激活成功，请马上登录！");
        } catch (UserException e) {
            // 说明service抛出了异常
            request.setAttribute("msg", e.getMessage());
            request.setAttribute("code", "error");//通知msg.jsp显示X
        }
        //return "d:/jsps/msg.jsp";
        request.getRequestDispatcher("/jsps/msg.jsp").forward(request,response);
    }

    //对表单逐个校验
    private Map<String,String> validateRegist(User formUser, HttpSession session){
        Map<String,String> errors =new HashMap<String, String>();  //存放错误信息
        //对表单中的用户名校验
        String loginname = formUser.getLoginname();
        String sessionCode = (String) session.getAttribute("vCode");
        if (loginname==null||loginname.trim().isEmpty()){
            errors.put("loginname","用户名不能为空");
        }else if (loginname.length()<3){
            errors.put("loginname","用户名为3-20位");
        }else if (!userService.validateLoginname(loginname)){
            errors.put("loginname","用户名已存在");
        }
        //对表单中的密码校验
        String loginpass = formUser.getLoginpass();
        if (loginpass==null||loginpass.trim().isEmpty()){
            errors.put("loginpass","密码不能为空");
        }else if (loginpass.length() < 3 || loginpass.length() > 20){
            errors.put("loginpass","用户名为3-20位");
        }
        //对表单中的确认密码验证
        String reloginpass = formUser.getReloginpass();
        if (reloginpass==null||reloginpass.trim().isEmpty()){
            errors.put("reloginpass","密码不能为空");
        }else if (!reloginpass.equals(loginpass)){
            errors.put("reloginpass","两次密码不一致");
        }
        String verifyCode = formUser.getVerifyCode();
        if (verifyCode==null||verifyCode.trim().isEmpty()){
            errors.put("verifyCode","验证码不能为空");
        }else if (!verifyCode.equalsIgnoreCase(sessionCode)){
            errors.put("verifyCode","验证码错误");
        }
        return errors;
    }
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.封装表单参数到User
        User userForm = CommonUtils.toBean(request.getParameterMap(), User.class);
        //2.校验表单数据
        Map<String, String> errors = validateLogin(userForm, request.getSession());
        if (errors.size()>0){
            request.setAttribute("errors",errors);
            request.setAttribute("form",userForm);  //把错误的信息回显到input
            request.getRequestDispatcher("/jsps/user/login.jsp").forward(request,response);
        }
        //3.使用service查询，判断用户是否存在
        User user = userService.login(userForm);
        //4.如果不存在 保存错误信息，用户名或密码错误
        if (user==null){ //用户账户或密码错误
            request.setAttribute("msg","用户名或密码错误");
            request.setAttribute("user",userForm);
            request.getRequestDispatcher("/jsps/user/login.jsp").forward(request,response);
        }else {//判断用户是否激活
            if (!user.isStatus()){//用户未激活
                request.setAttribute("msg","用户未激活");
                request.setAttribute("user",userForm);
                request.getRequestDispatcher("/jsps/user/login.jsp").forward(request,response);
            }else {//成功登录
                //保存用户到session
                request.getSession().setAttribute("sessionuser",user);
                String loginname = user.getLoginname();
                loginname=URLEncoder.encode(loginname,"utf-8");
                Cookie cookie=new Cookie("loginname",loginname);
                cookie.setMaxAge(10*60*60*24*10);//保存十天
                response.addCookie(cookie);
                response.sendRedirect(request.getContextPath()+"/index.jsp");
            }
        }
            //保存数据，回显
            //转发到login.jsp
        //5.如果存在，判断用户是否激活
            //保存数据，回显
            //转发到login.jsp
        //6.登录成功 保存当前查询出的user到session  保存当前用户的名称到cookie中，注意中文编码需要处理
    }
    //对登录表单逐个校验
    private Map<String,String> validateLogin(User formUser, HttpSession session){
        Map<String,String> errors =new HashMap<String, String>();  //存放错误信息
        /*//对表单中的用户名校验
        String sessionCode = (String) session.getAttribute("vCode");
        String loginname = formUser.getLoginname();
        if (loginname==null||loginname.trim().isEmpty()){
            errors.put("loginname","用户名不能为空");
        }else if (loginname.length()<3){
            errors.put("loginname","用户名为3-20位");
        }
        //对表单中的密码校验
        String loginpass = formUser.getLoginpass();
        if (loginpass==null||loginpass.trim().isEmpty()){
            errors.put("loginpass","密码不能为空");
        }else if (loginpass.length() < 3 || loginpass.length() > 20){
            errors.put("loginpass","用户名为3-20位");
        }
        //校验验证码
        String verifyCode = formUser.getVerifyCode();
        if (verifyCode==null||verifyCode.trim().isEmpty()){
            errors.put("verifyCode","验证码不能为空");
        }else if (!verifyCode.equalsIgnoreCase(sessionCode)){
            errors.put("verifyCode","验证码错误");
        }*/
        return errors;
    }
    /*
     * @Param [request, response]
     * @return void
     * 修改密码功能
    */
    public void updatePass(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User userForm = CommonUtils.toBean(request.getParameterMap(), User.class);
        User user = (User) request.getSession().getAttribute("sessionuser");
        if (user==null){
            request.setAttribute("msg","你还没登录");
            request.getRequestDispatcher("/goods/jsps/user/login.jsp");
        }
        try {
            userService.updatePass(user.getUid(),userForm.getNewpass(),userForm.getLoginpass());
            request.setAttribute("msg","修改密码成功");
            request.setAttribute("code","success");
            request.getRequestDispatcher("/jsps/msg.jsp").forward(request,response);
        } catch (UserException e) {
            request.setAttribute("msg",e.getMessage());     //保存异常信息到request
            request.setAttribute("user",userForm);   //把数据回显到input
            request.getRequestDispatcher("/jsps/user/pwd.jsp").forward(request,response);
        }
    }
    /*
     * @Param [request, response]
     * @return void
     * 用户退出功能
    */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().invalidate();//销毁session
        response.sendRedirect(request.getContextPath()+"/jsps/user/login.jsp");
    }
}
