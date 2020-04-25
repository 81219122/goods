package com.zhangrun.admin.controller.admin;

import cn.itcast.servlet.BaseServlet;
import com.zhangrun.entity.Order;
import com.zhangrun.entity.PageBean;
import com.zhangrun.service.impl.OrderServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/3/4 13:09
 */
@WebServlet("/adminOrderServlet")
public class AdminOrderServlet extends BaseServlet {
    private OrderServiceImpl orderService=new OrderServiceImpl();
    /*
     * @Param [request]
     * @return int
     * 获取当前页码
     */
    private int getPc(HttpServletRequest request){
        int pc=1;
        String param = request.getParameter("pc");
        if (param!=null && !param.trim().isEmpty()){
            try {
                pc=Integer.parseInt(param);
            }catch (RuntimeException e){}
        }
        return pc;
    }

    /*
     * @Param [request]
     * @return java.lang.String
     * 截取url  页面中的分页导航中需要使用它作为超链接的目标
     */
    private String getUrl(HttpServletRequest request){
        String url =null;
        String requestURI = request.getRequestURI(); //该方法得到  /goods/bookServlet
        String queryString = request.getQueryString();  //该方法得到method?方法名&参数
        url= requestURI + "?"+queryString;
        int index=url.lastIndexOf("&pc");  //要截取掉pc这个参数
        if (index!=-1){
            url=url.substring(0,index);
        }
        return url;
    }
    public void findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int pc = getPc(request);
        String url = getUrl(request);
        PageBean<Order> all = orderService.findAll(pc);
        all.setUrl(url);
        request.setAttribute("pb",all);
        request.getRequestDispatcher("/adminjsps/admin/order/list.jsp").forward(request,response);
    }

    public void findByStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int pc = getPc(request);
        String url = getUrl(request);
        int status = Integer.parseInt(request.getParameter("status"));
        PageBean<Order> pb = orderService.findByStatus(status, pc);
        pb.setUrl(url);
        request.setAttribute("pb",pb);
        request.getRequestDispatcher("/adminjsps/admin/order/list.jsp").forward(request,response);
    }

    /*
     * @Param [request, response]
     * @return void
     * 加载当前订单的订单条目
    */
    public void loadOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String oid = request.getParameter("oid");
        Order order = orderService.loadOrder(oid);
        request.setAttribute("order",order);
        String btn=request.getParameter("btn");  //获取它来判断用户点击了哪个按钮
        request.setAttribute("btn",btn);
        request.getRequestDispatcher("/adminjsps/admin/order/desc.jsp").forward(request,response);
    }

    /*
     * @Param [request, response]
     * @return void
     * 管理员取消订单功能
    */
    public void cancel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String oid = request.getParameter("oid");
        int status = orderService.findStatus(oid);
        if (status!=1){
            request.setAttribute("msg","该订单不能被取消");
            request.getRequestDispatcher("/adminjsps/msg.jsp").forward(request,response);
        }else {
            orderService.updateOrderStatus(oid,5);
            request.setAttribute("msg","订单已取消");
            request.getRequestDispatcher("/adminjsps/msg.jsp").forward(request,response);
        }
    }

    /*
     * @Param [request, response]
     * @return void
     *  发货
    */
    public void send(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String oid = request.getParameter("oid");
        int status = orderService.findStatus(oid);
        if (status!=2){
            request.setAttribute("msg","该订单暂不支持发货");
            request.getRequestDispatcher("/adminjsps/msg.jsp").forward(request,response);
        }else {
            orderService.updateOrderStatus(oid,3);
            request.setAttribute("msg","订单已发货");
            request.getRequestDispatcher("/adminjsps/msg.jsp").forward(request,response);
        }
    }
}
