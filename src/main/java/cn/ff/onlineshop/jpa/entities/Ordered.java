package cn.ff.onlineshop.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Entity
@Table(name = "ordered")
public class Ordered {

	/*`oid` varchar(32) NOT NULL,
	  `ordertime` datetime DEFAULT NULL,
	  `total` double DEFAULT NULL,
	  `state` int(11) DEFAULT NULL,
	  `address` varchar(30) DEFAULT NULL,
	  `name` varchar(20) DEFAULT NULL,
	  `telephone` varchar(20) DEFAULT NULL,
	  `uid` varchar(32) DEFAULT NULL*/
	@Id
	@Column
	private String oid;//该订单的订单号
	@Column
	private Date ordertime;//下单时间
	@Column
	private double total;//该订单的总金额
	@Column
	private int state;//订单支付状态 1代表已付款 0代表未付款
	@Column
	private String address;//收货地址
	@Column
	private String name;//收货人
	@Column
	private String telephone;//收货人电话
	@Column
	private String userid;
	//private User user;//该订单属于哪个用户
	
	//该订单中有多少订单项
	//List<OrderItem> orderItems = new ArrayList<OrderItem>();


	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public Date getOrdertime() {
		return ordertime;
	}

	public void setOrdertime(Date ordertime) {
		this.ordertime = ordertime;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@Override
	public String toString() {
		return "Ordered{" +
				"oid='" + oid + '\'' +
				", ordertime=" + ordertime +
				", total=" + total +
				", state=" + state +
				", address='" + address + '\'' +
				", name='" + name + '\'' +
				", telephone='" + telephone + '\'' +
				", userid='" + userid + '\'' +
				'}';
	}
}
