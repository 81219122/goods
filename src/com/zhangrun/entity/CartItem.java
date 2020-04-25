package com.zhangrun.entity;

import java.math.BigDecimal;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/24 13:48
 */
public class CartItem {
    private String cartItemId;//主键
    private int quantity;  //数量
    private Book book;  //条目对应的图书  bid
    private User user;   //对应用户对象
    /*
     * @Param []
     * @return double
     * 添加小计方法
    */
    public double getSubtotal(){
        //使用BigDecimal 不会有误差 要求必须使用String类型构造器
        BigDecimal b1=new BigDecimal(book.getCurrPrice()+"");
        BigDecimal b2=new BigDecimal(quantity+"");
        BigDecimal b3=b1.multiply(b2);  //相乘
        return b3.doubleValue();
    }
    @Override
    public String toString() {
        return "CartItem{" + "cartItemId='" + cartItemId + '\'' + ", quantity=" + quantity + ", book=" + book + ", user=" + user + '}';
    }

    public String getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(String cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
