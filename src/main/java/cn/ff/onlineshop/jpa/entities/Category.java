package cn.ff.onlineshop.jpa.entities;

import javax.persistence.*;

@Entity
@Table(name = "category")
public class Category {
	@Id
	private String cid;
	@Column
	private String cname;
	@Column
	private Integer num;

	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
}
