package com.zhangrun.dao.impl;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import com.sun.org.apache.xpath.internal.operations.Or;
import com.zhangrun.dao.IOrderDao;
import com.zhangrun.entity.*;
import org.apache.commons.dbutils.handlers.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/27 15:37
 */
public class OrderDaoImpl implements IOrderDao {
    private TxQueryRunner txQueryRunner=new TxQueryRunner();

    /*
     * @Param [map]
     * @return com.zhangrun.entity.OrderItem
     * 把一个map封装成一个orderitem对象
    */
    private OrderItem toOrderItem(Map<String,Object> map){
        if (map==null||map.size()==0)return null;
        OrderItem orderItem = CommonUtils.toBean(map, OrderItem.class);
        Book book = CommonUtils.toBean(map, Book.class);
        orderItem.setBook(book);
        return orderItem;
    }
    /*
     * @Param
     * @return
     * 把多个map 封装成多个OrderItem对象
    */
    private List<OrderItem> toOrderItemList(List<Map<String,Object>> mapList){
        List<OrderItem> list=new ArrayList<OrderItem>();
        for (Map<String,Object> maps:mapList){
            OrderItem orderItem = toOrderItem(maps);
            list.add(orderItem);
        }
        return list;
    }

    /*
     * @Param [uid, pc]
     * @return com.zhangrun.entity.PageBean<com.zhangrun.entity.Order>
     *     查询该用户的订单条目
    */
    @Override
    public PageBean<Order> findByUser(String uid, int pc) throws SQLException {
        List<Expression> expressionList=new ArrayList<Expression>();
        expressionList.add(new Expression("uid" ,"=",uid));
        PageBean<Order> byCriteria = findByCriteria(expressionList, pc);
        return byCriteria;
    }

    @Override
    public PageBean<Order> findByCriteria(List<Expression> expressionList, int pc) throws SQLException {
        int ps=PageConstants.order_page_size;  //设置每页记录数
        //2.通过expression来生成where子句
        StringBuilder wheresql=new StringBuilder(" where 1 = 1");  //1=1  因为条件后面要加and处理
        List<Object> params=new ArrayList<Object>();
        for (Expression e:expressionList){
            wheresql.append(" and ").append(e.getName()).append(" ").append(e.getOperator()).append(" ");
            if (!e.getOperator().equals("is null")){ //如果运算符是is null 就不给它?占位符
                wheresql.append("?");
                params.add(e.getValue()); //把条件的值放到params中
            }
        }
        //3.总记录数tr
        String sql="select count(*) from t_order " +wheresql;
        Number number=(Number) txQueryRunner.query(sql,new ScalarHandler(),params.toArray());
        int tr = number.intValue();  //得到总记录数

        //4.得到beanList，即当前页记录
        sql="select * from t_order" +wheresql + " order by ordertime desc limit ?,?";
        params.add((pc-1)*ps); //当前页首页记录的下标
        params.add(ps);  //一共查询几行，即每页记录数
        List<Order> beanlist=txQueryRunner.query(sql,new BeanListHandler<Order>(Order.class),params.toArray());
        //得到了Order 但是没有得到每个订单的条目 要加载出来
        for (Order order:beanlist){
            loadOrderItem(order);
        }

        //5.创建PageBean，设置参数
        PageBean<Order> pageBean=new PageBean();
        pageBean.setPs(ps);
        pageBean.setPc(pc);
        pageBean.setTr(tr);
        pageBean.setBeanList(beanlist);
        return pageBean;
    }

    /*
     * @Param [order]
     * @return void
     * 创建新的订单
    */
    @Override
    public void add(Order order) throws SQLException {
        //1.添加订单
        String sql="insert into t_order values(?,?,?,?,?,?)";
        Object [] params={order.getOid(),order.getOrdertime(),order.getTotal(),order.getStatus(),order.getAddress(),
        order.getOwner().getUid()};
        txQueryRunner.update(sql,params);
        //2.循环遍历订单的所有条目，让每个条目生成一个object[],多个条目就对应object[][] 执行批处理，完成插入订单条目
        sql="insert into t_orderitem values(?,?,?,?,?,?,?,?)";
        int size = order.getOrderItemList().size();
        Object objs [][]=new Object[size][];
        for (int i=0;i<size;i++){
            OrderItem item=order.getOrderItemList().get(i);
            objs [i]=new Object[]{item.getOrderItemId(),item.getQuantity(),item.getSubtotal(),item.getBook().getBid(),
            item.getBook().getBname(),item.getBook().getCurrPrice(),item.getBook().getImage_b(),item.getOrder().getOid()
            };
        }
        txQueryRunner.batch(sql,objs);
    }

    /*
     * @Param [oid]
     * @return com.zhangrun.entity.Order
     * 查询订单详情信息
    */
    @Override
    public Order findByOid(String oid) throws SQLException {
        String sql="select * from t_order where oid = ?";
        Order order = txQueryRunner.query(sql, new BeanHandler<Order>(Order.class),oid);
        //为当前订单加载所有条目
        loadOrderItem(order);
        return order;
    }

    /*
     * @Param [oid]
     * @return int
     * 根据订单oid查询用户的状态
    */
    @Override
    public int findStatus(String oid) throws SQLException {
        String sql="select status from t_order where oid = ?";
        Number number = (Number) txQueryRunner.query(sql, new ScalarHandler(), oid);
        return number.intValue();
    }

    /*
     * @Param [oid]
     * @return void
     * 修改订单状态
    */
    @Override
    public void updateOrderStatus(String oid,int status) throws SQLException {
        String sql="update t_order set status = ? where oid = ?";
        txQueryRunner.update(sql,status,oid);
    }

    /*
     * @Param [oid]
     * @return void
     * 删除订单
    */
    @Override
    public void deleteOrder(String oid) throws SQLException {
        String sql="delete from t_order where oid = ?";
        txQueryRunner.update(sql,oid);
    }

    /*
     * @Param [pc]
     * @return com.zhangrun.entity.PageBean<com.zhangrun.entity.Order>
     *   查询所有订单
    */
    @Override
    public PageBean<Order> findAll(int pc) throws SQLException {
        List<Expression> expressionList=new ArrayList<Expression>();
        PageBean<Order> byCriteria = findByCriteria(expressionList, pc);
        return byCriteria;
    }

    /*
     * @Param [status, pc]
     * @return com.zhangrun.entity.Order
     * 按状态查询订单
    */
    @Override
    public PageBean<Order> findByStatus(int status, int pc) throws SQLException {
        List<Expression> expressionList=new ArrayList<>();
        expressionList.add(new Expression("status","=",status+""));
        PageBean<Order> byCriteria = findByCriteria(expressionList, pc);
        return byCriteria;
    }

    /*
     * @Param [order]
     * @return void
     * 为订单加载所有条目
    */
    private void loadOrderItem(Order order) throws SQLException {
        String sql="select * from t_orderitem where oid = ?";
        List<Map<String, Object>> maps = txQueryRunner.query(sql, new MapListHandler(), order.getOid());
        List<OrderItem> list = toOrderItemList(maps);
        order.setOrderItemList(list);
    }

}
