package cn.ff.onlineshop.service;

import cn.ff.onlineshop.jpa.entities.Category;
import cn.ff.onlineshop.jpa.entities.Ordered;
import cn.ff.onlineshop.jpa.entities.Product;
import cn.ff.onlineshop.jpa.bean.PageBean;
import cn.ff.onlineshop.jpa.vo.OrderItemInfoVo;
import cn.ff.onlineshop.jpa.vo.OrderedVo;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    /*获得热门商品*/
    List<Product> findHotProductList();

    /*获得最新商品*/
    List<Product> findNewProductList();

    /*获得所有分类*/
    List<Category> findAllCategory();

    /*获得展示分类*/
    List<Category> findShowCategory(int count);

    /*获得商品分类*/
    Category findByCid(String cid);

    /*获得分页信息*/
    PageBean findProductListByCid(String cid, int currentPage, int currentCount);

    /*获取单个商品*/
    Product findProductByPid(String pid);

    /*获取所有分页索引*/
    List<Integer> getPageList(String cid, int currentCount);

    /*提交订单,将数据存到数据库中*/
    void submitOrder(OrderedVo orderedVo);

    /*修改收货信息*/
    void updateOrderAdrr(Ordered ordered);

    /*修改订单状态 1已付款  0未付款*/
    void updateOrderState(String r6_Order);

    /*获得指定用户的订单集合*/
    List<Ordered> findAllOrdersByUid(String uid);

    /*根据订单id查询详细订单项*/
    List<OrderItemInfoVo> findByOrderid(String oid);

    /*根据商品名称搜索商品*/
    Page<Product> findProductByNameLike(int pagenum, int pigesize,String key);
}
