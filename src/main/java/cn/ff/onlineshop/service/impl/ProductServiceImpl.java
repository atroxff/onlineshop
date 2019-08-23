package cn.ff.onlineshop.service.impl;

import java.util.ArrayList;
import java.util.List;

import cn.ff.onlineshop.jpa.entities.Category;
import cn.ff.onlineshop.jpa.entities.OrderItem;
import cn.ff.onlineshop.jpa.entities.Ordered;
import cn.ff.onlineshop.jpa.entities.Product;
import cn.ff.onlineshop.jpa.reposity.CategoryRepository;
import cn.ff.onlineshop.jpa.reposity.OrderItemRepository;
import cn.ff.onlineshop.jpa.reposity.OrderedRepository;
import cn.ff.onlineshop.jpa.reposity.ProductRepository;
import cn.ff.onlineshop.jpa.bean.PageBean;
import cn.ff.onlineshop.jpa.vo.OrderItemInfoVo;
import cn.ff.onlineshop.jpa.vo.OrderItemVo;
import cn.ff.onlineshop.jpa.vo.OrderedVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements cn.ff.onlineshop.service.ProductService {
	@Autowired
	ProductRepository productRepository;
	@Autowired
	OrderedRepository orderedRepository;
	@Autowired
	OrderItemRepository orderItemRepository;
	@Autowired
	CategoryRepository categoryRepository;

	/*获得热门商品*/
	@Override
	public List<Product> findHotProductList() {
		return productRepository.findHotProductList(1,0,9);//从0开始向后9个共10个
	}

	/*获得最新商品*/
	@Override
	public List<Product> findNewProductList() {
		return productRepository.findNewProductList(0,9);
	}

	/*获得所有分类*/
	@Override
	public List<Category> findAllCategory() {
		return categoryRepository.findAll();
	}

	@Override
	public List<Category> findShowCategory(int count) {
		return categoryRepository.findShowCategory(count);
	}

	/*获得商品分类*/
	@Override
	public Category findByCid(String cid) {
		return categoryRepository.findByCid(cid);
	}

	/*获得分页信息*/
	@Override
	public PageBean findProductListByCid(String cid, int currentPage, int currentCount) {

		//封装一个PageBean 返回web层
		PageBean<Product> pageBean = new PageBean<Product>();
		
		//1、封装当前页
		pageBean.setCurrentPage(currentPage);
		//2、封装每页显示的条数
		pageBean.setCurrentCount(currentCount);
		//3、封装总条数
		int totalCount = productRepository.countByCategoryid(cid) ;
		pageBean.setTotalCount(totalCount);
		//4、封装总页数
		int totalPage = (int) Math.ceil(1.0*totalCount/currentCount);
		pageBean.setTotalPage(totalPage);
		
		//5、当前页显示的数据
		// select * from product where cid=? limit ?,?
		// 当前页与起始索引index的关系
		int index = (currentPage-1)*currentCount;
		List<Product> list = productRepository.findByCategoryid(cid,index,currentCount);
		pageBean.setList(list);

		return pageBean;
	}

	/*获取单个商品*/
	@Override
	public Product findProductByPid(String pid) {
		return productRepository.findByPid(pid);
	}

	/*获取所有分页索引*/
	@Override
	public List<Integer> getPageList(String cid, int currentCount) {
		List<Integer> pageList = new ArrayList();
		int totalCount = productRepository.countByCategoryid(cid) ;//总条数
		int totalPage = (int) Math.ceil(1.0*totalCount/currentCount);//总页数
		for(int i=1;i<=totalPage;i++){
			pageList.add(i);
		}
		return pageList;
	}

	/*提交订单,将数据存到数据库中Ordered Ordered_item*/
	@Override
	public void submitOrder(OrderedVo orderedVo) {
		/*
		private String oid;//该订单的订单号
		private Date ordertime;//下单时间
		private double total;//该订单的总金额
		private int state;//订单支付状态 1代表已付款 0代表未付款
		private String address;//收货地址
		private String name;//收货人
		private String telephone;//收货人电话
		private String userid;
		*/
		Ordered ordered = new Ordered();
		BeanUtils.copyProperties(orderedVo,ordered);
		orderedRepository.save(ordered);

		List<OrderItemVo> orderItems = orderedVo.getOrderItems();
		for(OrderItemVo orderItemVo:orderItems){
			OrderItem orderItem =new OrderItem();
			BeanUtils.copyProperties(orderItemVo,orderItem);
			orderItemRepository.save(orderItem);
		}

	}

	/*修改收货信息*/
	public void updateOrderAdrr(Ordered ordered){
		orderedRepository.updateOrderAdrr(ordered.getOid(), ordered.getAddress(), ordered.getName(), ordered.getTelephone());
	}

	/*修改订单状态 1已付款  0未付款*/
	@Override
	public void updateOrderState(String oid){
		orderedRepository.updateOrderState(oid,1);
	}

	/*获得指定用户的订单集合*/
	@Override
	public List<Ordered> findAllOrdersByUid(String uid){
		return orderedRepository.findByUseridOrderByOrdertimeDesc(uid);
	}

	/*根据订单id查询详细订单项*/
	@Override
	public List<OrderItemInfoVo> findByOrderid(String oid){
		return orderItemRepository.findByOrderid(oid);
	}

    @Override
    public Page<Product> findProductByNameLike(int pagenum, int pigesize, String key) {
		PageRequest pageable = new PageRequest(pagenum, pigesize, Sort.Direction.DESC, "pdate");
		return productRepository.findByNameLike(key,pageable);
    }

}
