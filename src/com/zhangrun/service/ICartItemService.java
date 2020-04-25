package com.zhangrun.service;

import com.zhangrun.entity.CartItem;

import java.util.List;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/24 13:59
 */
public interface ICartItemService{
    //根据uid查询我的购物车
    List<CartItem> myCart(String uid);

    //添加条目
    void add(CartItem cartItem);

    //批量删除
    void batchDelete(String cartItemIds);

    //修改购物车条目数量
    CartItem updateQuantity(String cartItemId,int quantity);

    //加载多个CartItem
    List<CartItem> loadCartItems(String cartItemIds);
}
