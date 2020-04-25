package com.zhangrun.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/3/4 14:57
 */
/*
@WebFilter(filterName = "AdminFilter",urlPatterns = {"/adminjsps/admin/*","/AdminAddBookServlet","/AdminBookServlet","AdminCategoryServlet","/AdminOrderServlet"})
public class AdminFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request=(HttpServletRequest) req;
        Object admin = request.getSession().getAttribute("admin");
        if (admin==null){
            request.setAttribute("msg","你还没登录请登录!");
            request.getRequestDispatcher("/adminjsps/login.jsp").forward(request,resp);
            return;
        }else {
            chain.doFilter(req, resp);
        }

    }
}
*/
