package com.zhangrun.entity;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/27 15:02
 */
public class OrderItem {
    private String orderItemId;  //主键
    private int quantity; //数量
    private double subtotal; //小计
    private Book book; //所关联的图书信息
    private Order order; //所属订单

    @Override
    public String toString() {
        return "OrderItem{" + "orderItemId='" + orderItemId + '\'' + ", quantity=" + quantity + ", subtotal=" + subtotal + ", book=" + book + ", order=" + order + '}';
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
