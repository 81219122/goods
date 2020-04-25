package com.zhangrun.service;

import com.zhangrun.entity.Order;
import com.zhangrun.entity.PageBean;

import java.sql.SQLException;
import java.util.List;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/27 15:38
 */
public interface IOrderService {
    //我的订单
    PageBean<Order> myOrder(String uid,int pc);

    //生成订单
    void creatOrder(Order order);

    //加载订单
    Order loadOrder(String oid);

    //查询状态
    int findStatus(String oid);

    //修改订单状态
    void updateOrderStatus(String oid ,int status);

    //订单删除
    void deleteOrder(String oid);

    //查询所有订单
    PageBean<Order> findAll(int pc);

    //按状态查询订单
    PageBean<Order> findByStatus(int status, int pc);
}
