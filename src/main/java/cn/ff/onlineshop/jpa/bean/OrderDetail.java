package cn.ff.onlineshop.jpa.bean;

import cn.ff.onlineshop.jpa.entities.Ordered;
import cn.ff.onlineshop.jpa.vo.OrderItemInfoVo;

import java.util.List;

public class OrderDetail {
    Ordered order;
    List<OrderItemInfoVo> itemlist;

    public Ordered getOrder() {
        return order;
    }

    public void setOrder(Ordered order) {
        this.order = order;
    }

    public List<OrderItemInfoVo> getItemlist() {
        return itemlist;
    }

    public void setItemlist(List<OrderItemInfoVo> itemlist) {
        this.itemlist = itemlist;
    }
}
