package cn.ff.onlineshop.controller;

import cn.ff.onlineshop.jpa.entities.User;
import cn.ff.onlineshop.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.Serializable;

@Controller
public class BaseController implements Serializable {


    private static final long serialVersionUID = 368716025702437574L;

    @Autowired
    UserService userService;

    /* 默认首页 */
    @RequestMapping("/index.do")
    public String index(){
        return "login/login1";
    }

    /*跳转个人信息*/
    @GetMapping("/settings/info.do")
    public String showPersonInfo(Model model){

        return "settings/personinfo";
    }

    /*提交个人信息*/
    @PostMapping("/settings/info.do")
    public String updatePersonInfo(Model model, String nickname, String email, String sex, HttpSession session){

        //数据校验
        if(StringUtils.isEmpty(nickname)||StringUtils.isEmpty(email)||StringUtils.isEmpty(sex)){
            model.addAttribute("msg", "输入参数不正确");
            return "settings/personinfo";
        }
        //更新用户信息
        User user = (User) session.getAttribute("user");
        user.setNickname(nickname);
        user.setEmail(email);
        user.setSex(sex);

        userService.updateUserInfo(user);

        //更新session中的user
        session.setAttribute("user", user);

        //重定向到首页
        return "redirect:/items/index.do";
    }

    /*跳转设置*/
    @GetMapping("/settings/system.do")
    public String showSystemInfo(Model model) {
        return "settings/systeminfo";
    }

    /*提交设置*/
    @PostMapping("/settings/system.do")
    public String updateSystemInfo(){

        //数据校验

        //更新用户信息

        //重定向到首页
        return "redirect:/items/index.do";
    }

    /*密码修改页*/
    @GetMapping("/settings/resetPsd.do")
    public String showresetPsd(Model model) {
        return "settings/resetPsd";
    }

    /*密码修改*/
    @PostMapping("/settings/resetPsd.do")
    public String resetPsd(Model model,String newpsd,String renewpsd,String oldpsd,HttpSession session) {

        //数据校验
        if(StringUtils.isEmpty(newpsd)||StringUtils.isEmpty(oldpsd)){
            //错误页
        }

        //核对旧密码，通过则更改旧密码为新密码
        User user = (User) session.getAttribute("user");
        if (user.getPassword().equals(oldpsd)) {
            user.setPassword(newpsd);
            userService.updateUserInfo(user);
            session.setAttribute("user",user);
            return "redirect:/settings/resetPsd.do";
        }else{//旧密码输入错误
            model.addAttribute("newpsd",newpsd);
            model.addAttribute("renewpsd",renewpsd);
            model.addAttribute("oldpsd",oldpsd);
            model.addAttribute("msg", "旧密码错误");

            return "settings/systeminfo";
        }


    }

    /* 底部 关于我们*/
    @RequestMapping("/base/aboutMe.do")
    public String aboutMe(){
        return "commons/info";
    }

}


