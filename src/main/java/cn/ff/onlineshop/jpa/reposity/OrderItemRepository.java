package cn.ff.onlineshop.jpa.reposity;

import cn.ff.onlineshop.jpa.entities.OrderItem;
import cn.ff.onlineshop.jpa.vo.OrderItemInfoVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderItemRepository  extends JpaRepository<OrderItem,String>  {

    /*插入OrderItem数据*/
    @Override
    @Transactional
    <S extends OrderItem> S save(S entity);

    /*根据订单id查询详细订单项*/
    @Query("select  new cn.ff.onlineshop.jpa.vo.OrderItemInfoVo(i,p) from OrderItem i,Product p where i.productid=p.pid and i.orderid=?1")
    List<OrderItemInfoVo> findByOrderid(String oid);

}
