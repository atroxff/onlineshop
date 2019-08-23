package cn.ff.onlineshop.jpa.entities;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "test_user")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column
    private Integer id;
    @Column
    private String name;
    @Column
    private String sex;
    @Column
    private String phone;
    @Column
    private Date birth;
    @Column
    private String email;
    @Column
    private String uid;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private String nickname;
    @Column
    private int state;//是否激活
    @Column
    private String code;//激活码

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", phone='" + phone + '\'' +
                ", birth=" + birth +
                ", email='" + email + '\'' +
                ", uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", state=" + state +
                ", code='" + code + '\'' +
                '}';
    }
}
