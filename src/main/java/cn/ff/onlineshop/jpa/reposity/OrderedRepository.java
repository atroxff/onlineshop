package cn.ff.onlineshop.jpa.reposity;

import cn.ff.onlineshop.jpa.entities.Ordered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderedRepository extends JpaRepository<Ordered,String>  {

    /*插入order数据*/
    @Override
    @Transactional
    <S extends Ordered> S save(S entity);



    /*修改收货信息*/
    @Modifying//必须使用 @Modifying 进行修饰. 以通知 SpringData， 这是一个 UPDATE 或 DELETE 操作
    @Transactional
    @Query("update Ordered o set o.address=?2,o.name=?3,o.telephone=?4 where o.oid=?1")
    void updateOrderAdrr(String oid,String address,String name,String telephone);

    /*修改订单状态 1已付款  0未付款*/
    @Modifying
    @Transactional
    @Query("update Ordered o set o.state=?2 where o.oid=?1")
    void updateOrderState(String oid,int state);

    /*根据userid查找订单*/
    //@Query("select  o from Ordered o  where o.oid=?1 order by o.ordertime desc ")
    List<Ordered> findByUseridOrderByOrdertimeDesc(String uid);

}
