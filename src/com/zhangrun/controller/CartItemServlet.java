package com.zhangrun.controller;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import com.zhangrun.dao.impl.CartItemDaoImpl;
import com.zhangrun.entity.Book;
import com.zhangrun.entity.CartItem;
import com.zhangrun.entity.User;
import com.zhangrun.service.impl.CartItemServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/24 14:00
 */
@WebServlet("/cartItemServlet")
public class CartItemServlet extends BaseServlet {
    private CartItemServiceImpl cartItemService=new CartItemServiceImpl();
    //查询我的购物车功能
    public void myCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user=(User) request.getSession().getAttribute("sessionuser");
        String uid = user.getUid();
        List<CartItem> list = cartItemService.myCart(uid);
        request.setAttribute("cartItemList",list);
        request.getRequestDispatcher("/jsps/cart/list.jsp").forward(request,response);
    }

    /*
     * @Param [request, response]
     * @return void
     * 添加购物车条目及记录
    */
    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> map = request.getParameterMap();
        CartItem cartItem = CommonUtils.toBean(map, CartItem.class);
        User user=(User) request.getSession().getAttribute("sessionuser");
        Book book = CommonUtils.toBean(map, Book.class);
        cartItem.setUser(user);
        cartItem.setBook(book);
        cartItemService.add(cartItem);
        myCart(request,response);
    }
    /*
     * @Param [request, response]
     * @return void
     * 购物车删除功能 包含批量删除
    */
    public void batchDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cartItemIds = request.getParameter("cartItemIds");
        cartItemService.batchDelete(cartItemIds);
        myCart(request,response);
    }

    public void updateQuantity(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String cartItemId = request.getParameter("cartItemId");
        CartItem cartItem = cartItemService.updateQuantity(cartItemId, quantity);
        //给客户端返回一个json对象 里面封装了数量和小计
        StringBuilder sb=new StringBuilder("{");
        sb.append("\"quantity\"").append(":").append(cartItem.getQuantity());
        sb.append(",");
        sb.append("\"subtotal\"").append(":").append(cartItem.getSubtotal());
        sb.append("}");
        response.getWriter().print(sb);
    }
    public void loadCartItemList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cartItemIds = request.getParameter("cartItemIds");
        Double total= Double.valueOf(request.getParameter("total"));
        List<CartItem> cartItemList = cartItemService.loadCartItems(cartItemIds);
        request.setAttribute("cartItemList",cartItemList);
        request.setAttribute("total",total);
        request.setAttribute("cartItemIds",cartItemIds);
        request.getRequestDispatcher("/jsps/cart/showitem.jsp").forward(request,response);
    }
}
