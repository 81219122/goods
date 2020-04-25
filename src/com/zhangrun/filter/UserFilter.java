package com.zhangrun.filter;

import com.zhangrun.entity.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/29 14:35
 * 用户过滤器
 */
@WebFilter(filterName = "UserFilter",urlPatterns = {"/jsps/cart/*","/cartItemServlet","/jsps/order/*","/orderServlet"})
public class UserFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request=(HttpServletRequest) req;  //把参数转换成HttpServletRequest 不然获取不到session
        User user = (User) request.getSession().getAttribute("sessionuser");
        if (user ==null ){
            request.setAttribute("code","error");
            request.setAttribute("msg","您还没登录!");
            request.getRequestDispatcher("/jsps/msg.jsp").forward(request,resp);
        }
        chain.doFilter(req, resp); //放行
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
