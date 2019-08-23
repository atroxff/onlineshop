package cn.ff.onlineshop.service;

import java.util.List;

import cn.ff.onlineshop.jpa.entities.Ordered;
import cn.ff.onlineshop.jpa.entities.Product;
import cn.ff.onlineshop.jpa.vo.OrderItemInfoVo;
import org.springframework.data.domain.Page;


public interface AdminService {

	/*新建商品*/
	 Product saveProduct(Product product);

	/*删除商品*/
	void deleteProduct(String pid);

	/*查找商品总数量*/
	long countProductNum();


	/*分页查找商品*/
	Page<Product> findAllProduct(int pagenum, int pigesize);

	/*获得所有订单*/
	 List<Ordered> findAllOrders();

	/*获取订单信息*/
	 List<OrderItemInfoVo> findOrderInfoByOid(String oid);

	 /*根据关键词搜索*/
	Page<Product> findProductByKey(int pagenum, int pagesize, String key, String type);
}
