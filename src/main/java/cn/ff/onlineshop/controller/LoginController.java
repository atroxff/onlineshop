package cn.ff.onlineshop.controller;


import cn.ff.onlineshop.form.RegisterForm1;
import cn.ff.onlineshop.jpa.entities.User;
import cn.ff.onlineshop.exception.UserExistException;
import cn.ff.onlineshop.form.RegisterForm;
import cn.ff.onlineshop.service.UserService;
import cn.ff.onlineshop.tools.JSONObject;
import cn.ff.onlineshop.utils.SysUtil;
import cn.ff.onlineshop.utils.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController {


    @Autowired
    private UserService userService;

    @RequestMapping("/user/loginPage.do")
    public String loginPage(){
        return "login/login1";
    }


    //登录认证
    //@RequestMapping(value = "/user/login")
    @PostMapping("/user/login.do")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Map<String,Object> map, HttpSession session){

        //验证用户名密码
        User user = userService.authUser(username, password);
        if(user!=null){
            //验证用户状态是否激活
            if(user.getState()==0){
                map.put("msg","用户未激活，请联系管理员");
                map.put("username",username);
                return  "login/login1";
            }


            //登陆成功，防止表单重复提交，可以重定向到主页
            session.setAttribute("user",user);

            //重定向和页面转发 对应的都是跳转到handler
            return "redirect:/items/index.do";

        }else{
            //登陆失败

            map.put("msg","用户名密码错误");
            map.put("username",username);
            map.put("password",password);
            return  "login/login1";
        }

    }

    //注销登录
    @RequestMapping("/user/logout.do")
    public String logout(Map<String,Object> map,HttpSession session){

        session.removeAttribute("user");
        map.clear();
        map.put("msg", "注销成功");
        return  "redirect:/user/loginPage.do";
    }

    //跳转到注册页1
    @GetMapping("/user/register1.do")
    public String toRegister1(){
        return  "login/register1";
    }

    //注册
    @RequestMapping(value="/user/register1.do",method = RequestMethod.POST)
    public String registerSubmit1( @Valid RegisterForm1 registerForm, BindingResult result, Map<String,Object> map,Model model){
        Map<String, String> errors = new HashMap();
        map.put("form", registerForm);
        //1.表单自动校验
        //2.校验失败

        if(result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            System.out.println(errors);
            map.put("errors", errors);
            //System.out.println(errors);
            //System.out.println(result.getFieldError());
            return  "login/register1";
        }

        //3.校验成功，调用service处理注册请求
        User user = new User();
        WebUtil.copyBean(registerForm, user);

        user.setUid(SysUtil.getUUID());
        user.setState(1);
        String activeCode = SysUtil.getUUID().substring(0,6);
        user.setCode(activeCode);
        user.setNickname(activeCode);

        try {

            //6.通过二次校验，注册成功
            userService.register(user);


            //返回注册成功页面
            map.put("msg","注册成功");
            return  "message";

        } catch (UserExistException e) {
            //4.用户已存在
            errors.put("username","用户名已存在");
            map.put("errors", errors);
            return  "login/register1";
        }catch(Exception e){
            //5.其他错误消息，跳转到消息提示页面
            map.put("msg", "服务器出错，请联系管理员");
            return  "message";

        }

    }

    //ajax校验用户名是否存在
    @RequestMapping(value="/user/checkUsername.do",method = RequestMethod.POST)
    public @ResponseBody  Object checkUsername(String username){
        JSONObject data = new JSONObject();
        boolean isExits = userService.isExitsUsername(username);
        data.put("isExits",isExits);
        return data;
    }

    /*
    获取手机验证码
     */
//    @RequestMapping("/user/ajax/getMsgNum.do")
//    public Object  getMsgNum(String msg){
//        System.out.println("getMsgNum");
//        System.out.println(msg);
//        JSONObject jsonObject=new JSONObject();
//        jsonObject.put("msg", msg);
//
//        return jsonObject ;
//    }


}
