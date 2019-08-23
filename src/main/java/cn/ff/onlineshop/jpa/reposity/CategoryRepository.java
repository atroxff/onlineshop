package cn.ff.onlineshop.jpa.reposity;

import cn.ff.onlineshop.jpa.entities.Category;
import cn.ff.onlineshop.jpa.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,String> {
    /* 查找单个分类菜单*/
    Category findByCid(String cid);

    /* 查找排序靠前的分类菜单*/
    @Query(nativeQuery = true,value = "select * from category order by num asc  limit 0,?")
    List<Category> findShowCategory(int count);


    /*插入Category数据*/
    @Override
    @Transactional
    <S extends Category> S save(S entity);

    /*根据cid删除*/
    @Override
    @Transactional
    void delete(String s);


    /*根据名称查找分类*/
    Category findByCname(String cname);
}
