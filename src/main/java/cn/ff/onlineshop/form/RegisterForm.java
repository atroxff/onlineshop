package cn.ff.onlineshop.form;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;

/**
 * 注册表单对象
 */
public class RegisterForm {
    public String username;
    public String password;
    public String repassword;
    public String email;
    public String birth;
    public String nickname;
    public String verifycode;
    public String phone;

    public Map<String,String> errors =new HashMap();

    @Override
    public String toString() {
        return "RegisterForm{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", repassword='" + repassword + '\'' +
                ", email='" + email + '\'' +
                ", birth='" + birth + '\'' +
                ", nickname='" + nickname + '\'' +
                ", verifycode='" + verifycode + '\'' +
                ", errors=" + errors +
                '}';
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
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

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getVerifycode() {
        return verifycode;
    }

    public void setVerifycode(String verifycode) {
        this.verifycode = verifycode;
    }

    public boolean validate(){
        boolean isOK =true;
        if(this.username==null||this.username.trim().equals("")){ //trim去空格
            isOK=false;
            errors.put("username", "用户名不能为空");
        }else{
            if(!this.username.matches("[A-Za-z]{3,8}")){//正则表达式 用户必须是3-8位字母
                isOK=false;
                errors.put("username", "用户名只能是3-8位字母");
            }
        }

        if(this.password==null||this.password.trim().equals("")){
            isOK=false;
            errors.put("password", "密码不能为空");
        }else{
            if(!this.password.matches("\\d{3,8}")){//用户必须是3-8位数字
                isOK=false;
                errors.put("password", "密码只能是3-8位数字");
            }
        }

        if(this.repassword==null||this.repassword.trim().equals("")){
            isOK=false;
            errors.put("repassword", "重复密码不能为空");
        }else{
            if(!this.repassword.equals(this.password)){
                isOK=false;
                errors.put("repassword", "两次输入密码不一致");
            }
        }


        if(this.email==null||this.email.trim().equals("")){
            isOK=false;
            errors.put("email", "邮箱不能为空");
        }else{
            //xx@xx.com.cn    \\w+@\\w+(\\.\\w+)+
            //   \\w+ 任意字符出现多次    (\\.\\w+)+  .com.cn出现多次
            if(!this.email.matches("\\w+@\\w+(\\.\\w+)+")){//
                isOK=false;
                errors.put("email", "邮箱格式不正确");
            }
        }

        //生日可以为空，不为空则检验日期格式
        if(this.birth!=null&& !this.birth.trim().equals("")){
            try{

                DateLocaleConverter dic= new DateLocaleConverter();
                dic.convert(this.birth,"yyyy-MM-dd");//String转Date
            }catch(Exception e){
                isOK=false;
                errors.put("birth", "日期格式不正确");
            }
        }

        if(this.nickname==null||this.nickname.trim().equals("")){
            isOK=false;
            errors.put("nickname", "昵称不能为空");
        }else{
            if(!this.nickname.matches("[\u4e00-\u9fa5]+")){//昵称只能是汉字
                isOK=false;
                errors.put("nickname", "昵称只能是汉字");
            }
        }

        if(this.phone==null||this.phone.trim().equals("")){
            isOK=false;
            errors.put("phone", "手机号不能为空");
        }else{
            if(!this.phone.matches("\\d{11}$")){//手机号必须是11位数字
                isOK=false;
                errors.put("phone", "手机号必须是11位数字");
            }
        }
        return isOK;
    }
}
