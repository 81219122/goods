package com.zhangrun.dao;

import com.sun.org.apache.xpath.internal.operations.Or;
import com.zhangrun.entity.Expression;
import com.zhangrun.entity.Order;
import com.zhangrun.entity.PageBean;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/27 15:37
 */
public interface IOrderDao {
    //按用户查询订单
    PageBean<Order> findByUser(String uid,int pc) throws SQLException;

    //封装的查询语句
    PageBean<Order> findByCriteria(List<Expression> expressionList,int pc) throws SQLException;

    //添加订单
    void add(Order order) throws SQLException;

    //查询订单详情信息
    Order findByOid(String oid) throws SQLException;

    //查询状态
    int findStatus(String oid) throws SQLException;

    //修改订单状态
    void updateOrderStatus(String oid,int status) throws SQLException;

    //订单删除
    void deleteOrder(String oid) throws SQLException;

    //查询所有订单
    PageBean<Order> findAll(int pc) throws SQLException;

    //按状态查询订单
    PageBean<Order> findByStatus(int status,int pc) throws SQLException;
}
