package com.zhangrun.service.impl;

import cn.itcast.jdbc.JdbcUtils;
import com.zhangrun.dao.impl.OrderDaoImpl;
import com.zhangrun.entity.Order;
import com.zhangrun.entity.PageBean;
import com.zhangrun.service.IOrderService;

import java.sql.SQLException;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/27 15:38
 */
public class OrderServiceImpl implements IOrderService {
    private OrderDaoImpl orderDao=new OrderDaoImpl();

    /*
     * @Param [uid, pc]
     * @return com.zhangrun.entity.PageBean<com.zhangrun.entity.Order>
     *     查询我的订单
     *     事务处理
    */
    @Override
    public PageBean<Order> myOrder(String uid, int pc) {
        try {
            JdbcUtils.beginTransaction();
            PageBean<Order> order = orderDao.findByUser(uid, pc);
            JdbcUtils.commitTransaction();
            return order;
        }catch (SQLException e){
            try {
                JdbcUtils.rollbackTransaction();
            } catch (SQLException e1) {}
            throw new RuntimeException(e);
        }
    }

    /*
     * @Param [order]
     * @return void
     * 创建订单
     * 事务处理
    */
    @Override
    public void creatOrder(Order order) {
        try{
            JdbcUtils.beginTransaction();
            orderDao.add(order);
            JdbcUtils.commitTransaction();
        }catch (SQLException e){
            try {
                JdbcUtils.rollbackTransaction();
            } catch (SQLException e1) { }
            throw new RuntimeException(e);
        }
    }

    /*
     * @Param [oid]
     * @return com.zhangrun.entity.Order
     * 加载所有订单
    */
    @Override
    public Order loadOrder(String oid) {
        try {
            JdbcUtils.beginTransaction();
            Order order = orderDao.findByOid(oid);
            JdbcUtils.commitTransaction();
            return order;
        }catch (SQLException e){
            try {
                JdbcUtils.rollbackTransaction();
            } catch (SQLException e1) {}
            throw new RuntimeException(e);
        }
    }

    /*
     * @Param [oid]
     * @return int
     * 修改状态
    */
    @Override
    public int findStatus(String oid) {
        try {
            return orderDao.findStatus(oid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * @Param [oid, status]
     * @return void
     * 修改订单状态
    */
    @Override
    public void updateOrderStatus(String oid, int status) {
        try {
            orderDao.updateOrderStatus(oid,status);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * @Param [oid]
     * @return void
     * 订单删除
    */
    @Override
    public void deleteOrder(String oid) {
        try {
            orderDao.deleteOrder(oid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * @Param [pc]
     * @return com.zhangrun.entity.PageBean<com.zhangrun.entity.Order>
     *     后台功能 查询所有的订单
    */
    @Override
    public PageBean<Order> findAll(int pc) {
        try {
            JdbcUtils.beginTransaction();
            PageBean<Order> order = orderDao.findAll(pc);
            JdbcUtils.commitTransaction();
            return order;
        }catch (SQLException e){
            try {
                JdbcUtils.rollbackTransaction();
            } catch (SQLException e1) {}
            throw new RuntimeException(e);
        }
    }

    /*
     * @Param [status, pc]
     * @return com.zhangrun.entity.PageBean<com.zhangrun.entity.Order>
     *     后台功能 根据状态查询订单
    */
    @Override
    public PageBean<Order> findByStatus(int status, int pc) {
        try {
            JdbcUtils.beginTransaction();
            PageBean<Order> order = orderDao.findByStatus(status,pc);
            JdbcUtils.commitTransaction();
            return order;
        }catch (SQLException e){
            try {
                JdbcUtils.rollbackTransaction();
            } catch (SQLException e1) {}
            throw new RuntimeException(e);
        }
    }
}
