package cn.ff.onlineshop.jpa.vo;

import cn.ff.onlineshop.jpa.entities.Ordered;
import cn.ff.onlineshop.jpa.entities.User;

import java.util.ArrayList;
import java.util.List;


public class OrderedVo  extends Ordered {


    public User user;//该订单属于哪个用户
    //该订单中有多少订单项
    public List<OrderItemVo> orderItems = new ArrayList<OrderItemVo>();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderItemVo> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemVo> orderItems) {
        this.orderItems = orderItems;
    }
}
