package cn.ff.onlineshop.mapper.pojo;

import org.apache.ibatis.type.Alias;

import java.io.Serializable;

@Alias(value="user")//mybatis指定别名，要求在.properties配置type-aliases-package属性
public class User implements Serializable {

    private static final long serialVersionUID = 148963360292621911L;


    private Long id = null;
    private String userName = null;
    private String note = null;

    //性别枚举  使用typeHandler转换
    private String sex = null;

    public User() {
    }

    public User(Long id, String userName, String note, String sex) {
        this.id = id;
        this.userName = userName;
        this.note = note;
        this.sex = sex;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
