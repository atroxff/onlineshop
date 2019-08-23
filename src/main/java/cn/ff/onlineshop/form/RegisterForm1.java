package cn.ff.onlineshop.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class RegisterForm1 {
    @NotEmpty(message="用户名不能为空")
    private String username;

    @NotEmpty(message="密码不能为空")
    private String password;

    @NotEmpty(message="重复密码不能为空")
    private String repassword;

    @Email(message="邮箱格式不正确")
    @NotEmpty(message="邮箱不能为空")
    private String email;

    @NotEmpty(message="姓名不能为空")
    private String name;

    @NotEmpty(message="性别不能为空")
    private String sex;

    @NotEmpty(message="出生日期不能为空")
    private String birth;

    @NotEmpty(message="手机号不能为空")
    private String phone;

    private String code;

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

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "RegisterForm1{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", repassword='" + repassword + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", birth='" + birth + '\'' +
                ", phone='" + phone + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
