package com.zhangrun.service.impl;

import cn.itcast.commons.CommonUtils;
import com.zhangrun.dao.impl.CartItemDaoImpl;
import com.zhangrun.entity.CartItem;
import com.zhangrun.service.ICartItemService;

import java.sql.SQLException;
import java.util.List;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/24 13:59
 */
public class CartItemServiceImpl implements ICartItemService {
    private CartItemDaoImpl cartItemDao=new CartItemDaoImpl();
    /*
     * @Param [uid]
     * @return java.util.List<com.zhangrun.entity.CartItem>
     *     根据uid查询我的购物车功能
    */
    @Override
    public List<CartItem> myCart(String uid) {
        try {
            return cartItemDao.findByUser(uid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * @Param [cartItem]
     * @return void
     * 添加条目
    */
    @Override
    public void add(CartItem cartItem) {
        //1.判断该条目是否存在
        try {
            // dataCartItem对象是数据库查询到的对象 cartItem是页面传过来的对象
            CartItem dataCartItem = cartItemDao.findByUidAndBid(cartItem.getUser().getUid(), cartItem.getBook().getBid());
            if (dataCartItem==null){//2.如果数据库没有该条目
                cartItem.setCartItemId(CommonUtils.uuid());
                cartItemDao.addCartItem(cartItem);
            }else {//3.如果数据库查询到有该条目
                // 把传过来的数量和数据库已有的数量相加
                int quantity=cartItem.getQuantity()+dataCartItem.getQuantity();
                // 修改数量
                cartItemDao.updateQuantity(dataCartItem.getCartItemId(),quantity);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    /*
     * @Param [cartItemIds]
     * @return void
     * 批量删除
    */
    @Override
    public void batchDelete(String cartItemIds) {
        try {
            cartItemDao.batchDelete(cartItemIds);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * @Param [cartItemId, quantity]
     * @return com.zhangrun.entity.CartItem
     * 根据条目id修改数量 并返回条目对象
    */
    @Override
    public CartItem updateQuantity(String cartItemId, int quantity) {
        try {
            cartItemDao.updateQuantity(cartItemId,quantity);
            return cartItemDao.findByCartItemId(cartItemId);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /*
     * @Param [cartItemIds]
     * @return java.util.List<com.zhangrun.entity.CartItem>
     *     加载多个cartitem
    */
    @Override
    public List<CartItem> loadCartItems(String cartItemIds) {
        try {
            return cartItemDao.loadCartItems(cartItemIds);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
