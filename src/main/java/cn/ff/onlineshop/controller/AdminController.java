package cn.ff.onlineshop.controller;

import cn.ff.onlineshop.constant.ConfigParam;
import cn.ff.onlineshop.constant.Sysconfig;
import cn.ff.onlineshop.jpa.entities.Category;
import cn.ff.onlineshop.jpa.entities.Product;
import cn.ff.onlineshop.jpa.vo.ProductVo;
import cn.ff.onlineshop.service.AdminService;
import cn.ff.onlineshop.service.CategoryService;
import cn.ff.onlineshop.service.ProductService;
import cn.ff.onlineshop.tools.JSONObject;
import cn.ff.onlineshop.utils.SysUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;


@Controller
public class AdminController {
    @Autowired
    ProductService productService;
    @Autowired
    AdminService adminService;

    @Autowired
    CategoryService categoryService;
    @Autowired
    ConfigParam configParam;

    /*首页*/
    @RequestMapping("/admin/index.do")
    public  String index(){
        return "model1/index";
    }

    /*登录页*/
    @RequestMapping(value="/admin/login.do",method = RequestMethod.GET)
    public  String toLogin(){
        return "model1/login";
    }

    /*登录*/
    @RequestMapping(value="/admin/login.do",method = RequestMethod.POST)
    public  String login(Model model, String username, String password, HttpSession session){

        //数据回显
        model.addAttribute("username", username);
        model.addAttribute("password", password);
        //校验非空
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            model.addAttribute("msg", "请输入用户名和密码");
            return "model1/login";
        }
        //用户认证
        if (username.equals("admin") && password.equals("Abc123++")) {
            session.setAttribute("adminUser",true);
            return "model1/index";
        }else{
            model.addAttribute("msg", "请输入正确的用户名和密码");
            return "model1/login";
        }
    }

    /*退出登录*/
    @RequestMapping(value="/admin/logout.do")
    public  String loginOut(HttpSession session){
        session.removeAttribute("adminUser");
        return "model1/login";
    }

    /*商品管理*/
    @RequestMapping("/admin/product.do")
    public  String productManager(Model model,String pagenumStr,String pagesizeStr,String key,String type){

        //参数校验
        if(StringUtils.isEmpty(pagenumStr)){
            pagenumStr="1";//默认第一页
        }
        if(StringUtils.isEmpty(pagesizeStr)){
            pagesizeStr=configParam.getPagesize();//每页显示数量
        }
        int pagenum=Integer.parseInt(pagenumStr)-1;//page从0开始
        int pagesize=Integer.parseInt(pagesizeStr);
        int totalpage;
        long count;
        List<Product> productList = new ArrayList();
        Page<Product> page=null;

        //数据准备
        if(StringUtils.isEmpty(key)){//全部商品
            page = adminService.findAllProduct(pagenum, pagesize);
        }
        else{//搜索商品

            //数据准备
            page = adminService.findProductByKey(pagenum, pagesize,key,type);
            //数据回显
            model.addAttribute("key", key);
            model.addAttribute("type", type);
            String typeName= Sysconfig.SearchType.map.get(type);
            model.addAttribute("typeName", typeName);
        }

        totalpage = page.getTotalPages();//总页数
        count = page.getTotalElements();//总记录数
        productList = page.getContent();//当前页数据
        model.addAttribute("pagenumStr", pagenumStr);
        model.addAttribute("totalpage", totalpage);
        model.addAttribute("count", count);
        model.addAttribute("productList", productList);


        return "model1/product_mng";
    }


    /*商品管理-跳转添加页*/
    @RequestMapping(value = "/admin/product/add.do",method = RequestMethod.GET)
    public  String toAddPage(Model model){
        List<Category> categoryList = categoryService.findAllCategory();
        model.addAttribute("categoryList", categoryList);
        return "model1/product_add";
    }

    /*商品管理-添加商品*/
    @RequestMapping(value = "/admin/product/add.do",method = RequestMethod.POST)
    public  String addProduct(Model model, Product product, MultipartFile pimage_pic){
        //上传图片
        if(pimage_pic == null){
            return "model1/404";
        }

        //封装数据
        String pid = String.valueOf(System.currentTimeMillis());
        product.setPid(pid);

        Date pdate = new Date();
        product.setPdate(pdate);

        product.setPflag(1);

        String cid=product.getCategoryid();
        Category Category = categoryService.findCategoryByCid(cid);
        String cname =Category.getCname();
        product.setCname(cname);

        //准备商品图片
        //原始名称
        String originalFilename=pimage_pic.getOriginalFilename();

        if(pimage_pic!=null &&  originalFilename!=null && originalFilename.length()>0){
            //图片路径
            String pic_path = configParam.getFilepath();
            File file = new File(pic_path);
            if (!file.exists()) {
                file.mkdirs();
            }
            System.out.println(pic_path);
            String newFileName = SysUtil.getUUID()+originalFilename.substring(originalFilename.lastIndexOf("."));//uuid拼接文件后缀
            System.out.println(newFileName);
            //新图片
            File newFile =new File(pic_path+newFileName);

            //将内存数据写入磁盘,创建虚拟目录的图片
            try {
                pimage_pic.transferTo(newFile);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("上传失败");
            }

            //将新的图片写入数据库
            product.setPimage(newFileName);
        }

        //调用service保存商品
        adminService.saveProduct(product);

        return "redirect:/admin/product.do";
    }

    /*商品管理-删除商品*/
    @RequestMapping(value = "/admin/product/delete.do")
    public  String deleteProduct(Model model,String pid){
        System.out.println("删除商品："+pid);
        adminService.deleteProduct(pid);
        return "redirect:/admin/product.do";
    }

    /*商品管理-跳转修改页*/
    @RequestMapping(value = "/admin/product/update.do",method = RequestMethod.GET)
    public  String toUpdatePage(Model model,String pid){

        Product prod = productService.findProductByPid(pid);
        ProductVo product=new ProductVo();
        BeanUtils.copyProperties(prod,product);
        product.setCategory(categoryService.findCategoryByCid(prod.getCategoryid()));
        model.addAttribute("product", product);

        List<Category> categoryList = categoryService.findAllCategory();
        model.addAttribute("categoryList", categoryList);
        return "model1/product_chg";
    }

    /*商品管理-修改商品*/
    @RequestMapping(value = "/admin/product/update.do",method = RequestMethod.POST)
    public  String updateProduct(Model model, String pid, Product product, MultipartFile pimage_pic, String pimage_pic_text){

        //准备商品图片
        //原始名称
        product.setPdate(productService.findProductByPid(pid).getPdate());
        product.setCname(categoryService.findCategoryByCid(product.getCategoryid()).getCname());

        String originalFilename=pimage_pic.getOriginalFilename();

        if(pimage_pic!=null &&  originalFilename!=null && originalFilename.length()>0){
            //图片路径
            String pic_path = configParam.getFilepath();
            File file = new File(pic_path);
            if (!file.exists()) {
                file.mkdirs();
            }
            System.out.println(pic_path);
            String newFileName = SysUtil.getUUID()+originalFilename.substring(originalFilename.lastIndexOf("."));//uuid拼接文件后缀
            System.out.println(newFileName);
            //新图片
            File newFile =new File(pic_path+newFileName);

            //将内存数据写入磁盘,创建虚拟目录的图片
            try {
                pimage_pic.transferTo(newFile);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("上传失败");
            }

            //将新的图片写入数据库
            product.setPimage(newFileName);
        }else{
            String newFileName=pimage_pic_text;
            product.setPimage(newFileName);
        }

        //调用service保存商品
        adminService.saveProduct(product);

        return "redirect:/admin/product.do";
    }


    /*分类管理*/
    @RequestMapping("/admin/category.do")
    public  String categoryManager(Model model){
        List<Category> categoryList = categoryService.findAllCategory();
        model.addAttribute("categoryList",categoryList);
        return "model1/category_mng";
    }

    /*分类管理-添加*/
    @RequestMapping(value="/admin/category/ajax/add.do",method = RequestMethod.POST)
    public  @ResponseBody Object categoryAdd(Model model, String cname, String num){
        JSONObject data = new JSONObject();

        //校验数据
        if (StringUtils.isEmpty(cname) || StringUtils.isEmpty(num)) {
            data.put("success", false);
            return data;
        }

        //封装菜单类
        Category category =new Category();
        List<Category> categoryList = categoryService.findAllCategory();//得到最后一个菜单的cid迭代
        String cid=null;
        if(categoryList.size()!=0){
            cid = categoryList.get(categoryList.size()-1).getCid();
        }else{
            cid = "1";
        }
        cid = SysUtil.strNumAdd(cid);
        category.setCid(cid);
        category.setCname(cname);
        category.setNum(Integer.parseInt(num));

        //调用service添加菜单
        Category result = categoryService.findCategoryByCname(cname);

        if(result == null) {
            categoryService.addCategory(category);
            data.put("success", true);
        }else{
            data.put("success", false);
            data.put("msg", "菜单名称已存在！");
        }
        return data;
    }

    /*分类管理-删除*/
    @RequestMapping(value="/admin/category/remove.do")
    public  String categoryRemove(Model model, String cid){

        //校验数据
        if(StringUtils.isEmpty(cid)){
            model.addAttribute("msg", "系统异常：请求参数cid为空");
            return "model1/404.html";
        }


        //调用service删除分类
        categoryService.deleteCategory(cid);

        return "redirect:/admin/category.do";
    }

    /*分类管理-修改*/
    @RequestMapping(value="/admin/category/ajax/change.do")
    public  @ResponseBody Object categoryChange(Model model,String cid,String cname,String num){
        JSONObject data = new JSONObject();

        //校验数据
        if (StringUtils.isEmpty(cid)||StringUtils.isEmpty(cname) || StringUtils.isEmpty(num)) {
            data.put("success", false);
            return data;
        }
        //封装
        Category category =new Category();
        category.setCid(cid);
        category.setCname(cname);
        category.setNum(Integer.parseInt(num));

        //调用service修改菜单
        categoryService.updateCategory(category);
        data.put("success", true);
        return data;
    }

    /*分类管理-修改菜单展示个数*/
    @RequestMapping(value="/admin/category/ajax/changeShowSize.do")
    public  @ResponseBody Object categoryChange(Model model,String size) {
        JSONObject data = new JSONObject();
        int count=0;
        if(StringUtils.isEmpty(size)) {
            data.put("success", false);
            data.put("msg", "size格式不合法");
            return data;
        }
        if(count>=3 && count<=8) {
            Sysconfig.Category.Count = count;
            data.put("success", true);
        }
        else{
            data.put("success", false);
            data.put("msg", "size格式不合法");
        }
        return data;
    }

    /*人事管理*/
    @RequestMapping("/admin/people.do")
    public  String peopleManager(Model model){
        return "model1/people_mng";
    }


}
