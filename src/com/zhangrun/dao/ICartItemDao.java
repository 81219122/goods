package com.zhangrun.dao;

import com.zhangrun.entity.CartItem;

import java.sql.SQLException;
import java.util.List;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/24 13:59
 */
public interface ICartItemDao {
    //通过用户查询购物车条目
    List<CartItem> findByUser(String uid) throws SQLException;

    //查询某个用户的某本图书在购物车是否存在
    CartItem findByUidAndBid(String uid,String bid) throws SQLException;

    //修改指定条目的数量
    void updateQuantity(String cartItemId,int quantity) throws SQLException;

    //添加条目
    void addCartItem(CartItem cartItem) throws SQLException;

    //批量删除
    void batchDelete(String cartItemIds) throws SQLException;

    //按id查询条目
    CartItem findByCartItemId(String cartItemId) throws SQLException;

    //加载多个CartItem
    List<CartItem> loadCartItems(String cartItemIds) throws SQLException;
}
