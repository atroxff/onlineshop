package cn.ff.onlineshop.controller;

import cn.ff.onlineshop.constant.Sysconfig;
import cn.ff.onlineshop.jpa.bean.OrderDetail;
import cn.ff.onlineshop.jpa.entities.*;
import cn.ff.onlineshop.jpa.bean.Cart;
import cn.ff.onlineshop.jpa.bean.CartItem;
import cn.ff.onlineshop.jpa.bean.PageBean;
import cn.ff.onlineshop.jpa.vo.OrderItemInfoVo;
import cn.ff.onlineshop.jpa.vo.OrderItemVo;
import cn.ff.onlineshop.jpa.vo.OrderedVo;
import cn.ff.onlineshop.service.ProductService;
import cn.ff.onlineshop.service.UserService;
import cn.ff.onlineshop.utils.PaymentUtil;
import cn.ff.onlineshop.utils.SysUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Controller
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;

    /* 显示首页 */
    @RequestMapping("/items/index.do")
    public String index(Model model){
        //1.准备热门商品 最新商品 菜单信息
        List<Product> hotProductList = productService.findHotProductList();
        List<Product> newProductList = productService.findNewProductList();

        model.addAttribute("hotProductList", hotProductList);
        model.addAttribute("newProductList", newProductList);


        return "show/index.html";
    }

    /* 获取分类主页 */
    @RequestMapping("/items/list.do")
    public String getAll(Model model, String cid, String page, HttpServletRequest request){

        String currentPageStr = page;
        if(currentPageStr==null) currentPageStr="1";
        int currentPage = Integer.parseInt(currentPageStr);

        int currentCount = 7;//设置每页显示最大数量
        PageBean pageBean = productService.findProductListByCid(cid,currentPage,currentCount);

        List<Integer> pageList = productService.getPageList(cid,currentCount);

        Category category = productService.findByCid(cid);
        model.addAttribute("category", category);

        model.addAttribute("pageList",pageList);
        model.addAttribute("pageBean",pageBean);
        model.addAttribute("cid",cid);

        //定义一个记录历史商品信息的集合
        List<Product> historyProductList = new ArrayList<Product>();
        //获得客户端携带名字叫pids的cookie
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for(Cookie cookie:cookies){
                if("pids".equals(cookie.getName())){
                    String pids = cookie.getValue();//3-2-1
                    String[] split = pids.split("-");
                    for(String pid : split){
                        Product pro = productService.findProductByPid(pid);
                        historyProductList.add(pro);
                    }
                }
            }
        }

        //将历史记录的集合放到域中
        model.addAttribute("historyProductList", historyProductList);

        return "show/product_list";
    }

    /* 获取商品详细信息 */
    @RequestMapping("/items/info.do")
    public String getInfo(Model model, String cid, String pid, String page, HttpServletRequest request, HttpServletResponse response){

        //1.数据准备
        Product product = productService.findProductByPid(pid);
        model.addAttribute("cid", cid);
        model.addAttribute("page", page);
        model.addAttribute("product", product);

        Category category = productService.findByCid(cid);
        model.addAttribute("category",category);


        //获得客户端携带cookie---获得名字是pids的cookie
        String pids = pid;
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for(Cookie cookie : cookies){
                if("pids".equals(cookie.getName())){
                    pids = cookie.getValue();
                    //1-3-2 本次访问商品pid是8----->8-1-3-2
                    //1-3-2 本次访问商品pid是3----->3-1-2
                    //1-3-2 本次访问商品pid是2----->2-1-3
                    //将pids拆成一个数组
                    String[] split = pids.split("-");//{3,1,2}
                    List<String> asList = Arrays.asList(split);//[3,1,2]
                    LinkedList<String> list = new LinkedList<String>(asList);//[3,1,2]
                    //判断集合中是否存在当前pid
                    if(list.contains(pid)){
                        //包含当前查看商品的pid
                        list.remove(pid);
                        list.addFirst(pid);
                    }else{
                        //不包含当前查看商品的pid 直接将该pid放到头上
                        list.addFirst(pid);
                    }
                    //将[3,1,2]转成3-1-2字符串
                    StringBuffer sb = new StringBuffer();
                    for(int i=0;i<list.size()&&i<7;i++){
                        sb.append(list.get(i));
                        sb.append("-");//3-1-2-
                    }
                    //去掉3-1-2-后的-
                    pids = sb.substring(0, sb.length()-1);
                }
            }
        }


        Cookie cookie_pids = new Cookie("pids",pids);
        response.addCookie(cookie_pids);

        return "show/product_info";

    }

    /*获取分类信息*/
    @RequestMapping(value = "/items/ajax/getCategoryList.do",method = RequestMethod.POST)
    public  @ResponseBody Object getCategoryList(Model model,HttpSession session ){


        List<Category> categoryList = productService.findShowCategory(Sysconfig.Category.Count);

        return categoryList;
//
//        model.addAttribute("categoryList", categoryList);
//        return "commons/header.html";

    }

    @RequestMapping("/cart/add.do")
    public String getInfo(Model model,String pid,String buyNumstr, HttpServletRequest request, HttpServletResponse response,HttpSession session) {


        //封装商品对象
        Product product = productService.findProductByPid(pid);
        int buyNum=Integer.parseInt(buyNumstr);

        //计算小计
        double subtotal =product.getShop_price()*buyNum;

        //封装CartItem
        CartItem item = new CartItem();
        item.setProduct(product);
        item.setBuyNum(buyNum);
        item.setSubtotal(subtotal);



        //获得购物车---判断是否在session中已经存在购物车
        Cart cart = (Cart) session.getAttribute("cart");
        if(cart==null){
            cart = new Cart();
        }

        //将购物项放到车中---key是pid
        //先判断购物车中是否已将包含此购物项了 ----- 判断key是否已经存在
        //如果购物车中已经存在该商品----将现在买的数量与原有的数量进行相加操作
        Map<String, CartItem> cartItems = cart.getCartItems();

        double newsubtotal = 0.0;

        if(cartItems.containsKey(pid)){
            //取出原有商品的数量
            CartItem cartItem = cartItems.get(pid);
            int oldBuyNum = cartItem.getBuyNum();
            oldBuyNum+=buyNum;
            cartItem.setBuyNum(oldBuyNum);
            cart.setCartItems(cartItems);
            //修改小计
            //原来该商品的小计
            double oldsubtotal = cartItem.getSubtotal();
            //新买的商品的小计
            newsubtotal = buyNum*product.getShop_price();
            cartItem.setSubtotal(oldsubtotal+newsubtotal);

        }else{
            //如果车中没有该商品
            cart.getCartItems().put(product.getPid(), item);
            newsubtotal = buyNum*product.getShop_price();
        }

        //计算总计
        double total = cart.getTotal()+newsubtotal;
        cart.setTotal(total);


        //将车再次访问session
        session.setAttribute("cart", cart);


        return "redirect:/cart/cartlist.do";
    }
    @RequestMapping("/cart/cartlist.do")
    public String getInfo(HttpServletRequest request, HttpServletResponse response,HttpSession session) {
        return "show/cart";
    }
        //删除单一商品
    @RequestMapping("/cart/remove.do")
    public String remove(String pid,HttpServletRequest request, HttpServletResponse response,HttpSession session){

        //删除session中的购物车中的购物项集合中的item
        Cart cart = (Cart) session.getAttribute("cart");
        if(cart!=null){
            Map<String, CartItem> cartItems = cart.getCartItems();
            //需要修改总价
            cart.setTotal(cart.getTotal()-cartItems.get(pid).getSubtotal());
            //删除
            cartItems.remove(pid);
            cart.setCartItems(cartItems);

        }

        session.setAttribute("cart", cart);

        //跳转回cart.jsp
        return "show/cart";
    }

    //清空购物车
    @RequestMapping("/cart/clearCart.do")
    public String clearCart(HttpSession session) {
        session.removeAttribute("cart");
        //跳转回cart.jsp
        return "show/cart";
    }

    //提交订单
    @RequestMapping("/cart/submitOrder.do")
    public String submitOrder(HttpSession session) {


        User user = (User) session.getAttribute("user");
        //目的：封装好一个Order对象 传递给service层
        OrderedVo orderedVo =new OrderedVo();
        orderedVo.setUser(user);

        //OrderedVo ordered = (OrderedVo) new Ordered();

        //1、private String oid;//该订单的订单号
        String oid = SysUtil.getUUID();
        orderedVo.setOid(oid);

        //2、private Date ordertime;//下单时间
        orderedVo.setOrdertime(new Date());

        //3、private double total;//该订单的总金额
        //获得session中的购物车
        Cart cart = (Cart) session.getAttribute("cart");
        double total = cart.getTotal();
        orderedVo.setTotal(total);

        //4、private int state;//订单支付状态 1代表已付款 0代表未付款
        orderedVo.setState(0);

        //5、private String address;//收货地址
        orderedVo.setAddress(null);

        //6、private String name;//收货人
        orderedVo.setName(null);

        //7、private String telephone;//收货人电话
        orderedVo.setTelephone(null);

        //8、private User user;//该订单属于哪个用户
        orderedVo.setUserid(user.getUid());


        //9、该订单中有多少订单项List<OrderItem> orderItems = new ArrayList<OrderItem>();
        //获得购物车中的购物项的集合map
        Map<String, CartItem> cartItems = cart.getCartItems();
        for(Map.Entry<String, CartItem> entry : cartItems.entrySet()){
            //取出每一个购物项
            CartItem cartItem = entry.getValue();
            //创建新的订单项
            OrderItemVo orderItemVo = new OrderItemVo();
            //1)private String itemid;//订单项的id
            orderItemVo.setItemid(SysUtil.getUUID());
            //2)private int count;//订单项内商品的购买数量
            orderItemVo.setCount(cartItem.getBuyNum());
            //3)private double subtotal;//订单项小计
            orderItemVo.setSubtotal(cartItem.getSubtotal());
            //4)private Product product;//订单项内部的商品
            orderItemVo.setProduct(cartItem.getProduct());
            orderItemVo.setProductid(cartItem.getProduct().getPid());
            //5)private Ordered ordered;//该订单项属于哪个订单
            orderItemVo.setOrder(orderedVo);
            orderItemVo.setOrderid(orderedVo.getOid());

            //将该订单项添加到订单的订单项集合中
            orderedVo.getOrderItems().add(orderItemVo);

        }


        //order对象封装完毕
        //传递数据到service层
        productService.submitOrder(orderedVo);


        session.setAttribute("order", orderedVo);


        //页面跳转
        return "show/order_info";
    }

    //确认订单---更新收获人信息+在线支付
    @RequestMapping("/cart/confirmOrder.do")
    public String confirmOrder(HttpServletRequest request, HttpServletResponse response) {

        //1、更新收货人信息
        Map<String, String[]> properties = request.getParameterMap();
        OrderedVo order = new OrderedVo();
        try {
            BeanUtils.populate(order,properties);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        productService.updateOrderAdrr(order);

        //2、在线支付
		/*if(pd_FrpId.equals("ABC-NET-B2C")){
			//介入农行的接口
		}else if(pd_FrpId.equals("ICBC-NET-B2C")){
			//接入工行的接口
		}*/
        //.......

        //只接入一个接口，这个接口已经集成所有的银行接口了  ，这个接口是第三方支付平台提供的
        //接入的是易宝支付
        // 获得 支付必须基本数据
        String orderid = request.getParameter("oid");
        //String money = order.getTotal()+"";//支付金额
        String money = "0.01";//支付金额
        // 银行
        String pd_FrpId = request.getParameter("pd_FrpId");

        // 发给支付公司需要哪些数据
        String p0_Cmd = "Buy";
        String p1_MerId = ResourceBundle.getBundle("merchantInfo").getString("p1_MerId");
        String p2_Order = orderid;
        String p3_Amt = money;
        String p4_Cur = "CNY";
        String p5_Pid = "";
        String p6_Pcat = "";
        String p7_Pdesc = "";
        // 支付成功回调地址 ---- 第三方支付公司会访问、用户访问
        // 第三方支付可以访问网址
        String p8_Url = ResourceBundle.getBundle("merchantInfo").getString("callback");
        String p9_SAF = "";
        String pa_MP = "";
        String pr_NeedResponse = "1";
        // 加密hmac 需要密钥
        String keyValue = ResourceBundle.getBundle("merchantInfo").getString(
                "keyValue");
        String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
                p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
                pd_FrpId, pr_NeedResponse, keyValue);


        String url = "https://www.yeepay.com/app-merchant-proxy/node?pd_FrpId="+pd_FrpId+
                "&p0_Cmd="+p0_Cmd+
                "&p1_MerId="+p1_MerId+
                "&p2_Order="+p2_Order+
                "&p3_Amt="+p3_Amt+
                "&p4_Cur="+p4_Cur+
                "&p5_Pid="+p5_Pid+
                "&p6_Pcat="+p6_Pcat+
                "&p7_Pdesc="+p7_Pdesc+
                "&p8_Url="+p8_Url+
                "&p9_SAF="+p9_SAF+
                "&pa_MP="+pa_MP+
                "&pr_NeedResponse="+pr_NeedResponse+
                "&hmac="+hmac;

        //重定向到第三方支付平台
        //return "redirect:"+url;
        return "redirect:/cart/callback.do?oid="+orderid;
    }

    @RequestMapping("/cart/callback.do")
    public String callback(HttpServletRequest request, Model model, HttpSession session)  {
        //默认支付成功，回调
        boolean isValid= true;
        String msg = "";
        String oid =request.getParameter("oid");
        if(StringUtils.isEmpty(oid)){
            System.out.println("系统异常，确认订单失败");
        }

        if (isValid) {
            // 响应数据有效
             msg ="支付成功！请等待商家发货...";
             session.removeAttribute("cart");//支付成功自动清空购物车
             productService.updateOrderState(oid);
        } else {
            // 数据无效
             msg ="支付失败！";
        }

        model.addAttribute("msg",msg);
        model.addAttribute("oid",oid);
        return "show/order_result";
    }

    //获得我的订单
    @RequestMapping("/cart/myOrders.do")
    public String myOrders(HttpServletRequest request, Map<String,Object> map)  {

        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("user");


        //查询该用户的所有的订单信息(单表查询orders表)
        //集合中的每一个Order对象的数据是不完整的 缺少List<OrderItem> orderItems数据
        List<Ordered> orderList = productService.findAllOrdersByUid(user.getUid());
        List<OrderDetail> ordersDetailList = new ArrayList();
        //循环所有的订单 为每个订单填充订单项集合信息
        if(orderList!=null){
            for(Ordered order : orderList){
                //获得每一个订单的oid
                String oid = order.getOid();
                //查询该订单的所有的订单项---mapList封装的是多个订单项和该订单项中的商品的信息
                List<OrderItemInfoVo> itemsVolist = productService.findByOrderid(oid);

                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setItemlist(itemsVolist);

                ordersDetailList.add(orderDetail);
            }
        }

        System.out.println(orderList);
        //orderList封装完整了
        //request.setAttribute("orderList", ordersDetailList);
        map.put("orderList", ordersDetailList);
        return "show/order_list";

    }

    @RequestMapping("/dosearch.do")
    public String search(Model model,String key){
        //key校验
        if (StringUtils.isEmpty(key)) {
            return "redirect:/items/index.do";
        }

        //数据准备 根据商品名称搜索
        int pagenum=0;//page从0开始
        int pagesize=30;

        Page<Product> page = productService.findProductByNameLike(pagenum, pagesize, key);
        int totalpage = page.getTotalPages();//总页数
        Long count = page.getTotalElements();//总记录数
        List<Product> productList = page.getContent();//当前页数据

        model.addAttribute("key",key);
        model.addAttribute("totalpage",totalpage);
        model.addAttribute("count",count);
        model.addAttribute("productList",productList);

        return "show/search";
    }

}
