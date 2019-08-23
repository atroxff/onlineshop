package cn.ff.onlineshop.jpa.vo;

import cn.ff.onlineshop.jpa.entities.OrderItem;
import cn.ff.onlineshop.jpa.entities.Product;

import java.io.Serializable;

public class OrderItemInfoVo implements Serializable {
    /* i.count,i.subtotal,p.pimage,p.pname,p.shop_price */
    private OrderItem orderItem;
    private Product product;

    public OrderItemInfoVo() {
    }

    public OrderItemInfoVo(OrderItem orderItem, Product product) {
        this.orderItem = orderItem;
        this.product = product;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
