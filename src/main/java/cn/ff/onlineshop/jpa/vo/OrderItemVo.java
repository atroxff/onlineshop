package cn.ff.onlineshop.jpa.vo;

import cn.ff.onlineshop.jpa.entities.OrderItem;
import cn.ff.onlineshop.jpa.entities.Ordered;
import cn.ff.onlineshop.jpa.entities.Product;

public class OrderItemVo  extends OrderItem{

    Product product;
    Ordered order;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Ordered getOrder() {
        return order;
    }

    public void setOrder(Ordered order) {
        this.order = order;
    }
}
