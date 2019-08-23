package cn.ff.onlineshop.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "order_item")
public class OrderItem {

	/*`itemid` varchar(32) NOT NULL,
	  `count` int(11) DEFAULT NULL,
	  `subtotal` double DEFAULT NULL,
	  `pid` varchar(32) DEFAULT NULL,
	  `oid` varchar(32) DEFAULT NULL*/
	@Id
	@Column
	private String itemid;//订单项的id
	@Column
	private int count;//订单项内商品的购买数量
	@Column
	private double subtotal;//订单项小计
	@Column
	private String productid;
	//private Product product;//订单项内部的商品
	@Column
	private String orderid;
	//private Ordered order;//该订单项属于哪个订单


	public String getItemid() {
		return itemid;
	}

	public void setItemid(String itemid) {
		this.itemid = itemid;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public String getProductid() {
		return productid;
	}

	public void setProductid(String productid) {
		this.productid = productid;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
}
