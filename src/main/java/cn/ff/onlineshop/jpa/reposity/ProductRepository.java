package cn.ff.onlineshop.jpa.reposity;

import cn.ff.onlineshop.jpa.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,String> {
    /* 查找热门商品 分页 */
    @Query(nativeQuery = true,value = "select t.* from test_item t where t.is_hot=? limit ?,?")
    List<Product>  findHotProductList(int is_hot, int start, int count);

    /* 查找所有商品 分页 */
    List<Product> findAll();

    /* 查找所有商品 分页Pageable */
    @Query("select c from Product c")
    Page<Product> findProductsInPage(Pageable pageable);

    /* 根据id查找商品 分页*/
    @Query("select c from Product c where c.pid like %?1%")
    Page<Product> findByPidLike(String pid, Pageable pageable);

    /* 根据名称查找商品 分页*/
    @Query("select c from Product c where c.pname like %?1%")
    Page<Product> findByNameLike(String name, Pageable pageable);

    /* 根据分类查找商品 分页*/
    @Query("select c from Product c where c.categoryid =?1")
    Page<Product> findByCid(String cid, Pageable pageable);


    /* 查找最新商品 */
    @Query(nativeQuery = true,value = "select * from test_item order by pdate desc limit ?,?")
    List<Product> findNewProductList(int start, int count);

    /* 同类商品数量 */
    @Query(nativeQuery = true,value = "select count(*) from test_item where categoryid=?")
    int countByCategoryid(String cid);

    /* 根据种类查找商品 */
    @Query(nativeQuery = true,value = "select * from test_item where categoryid=? limit ?,?")
    List<Product> findByCategoryid(String cid, int start, int count);

    /* 根据id查找商品 */
    Product findByPid(String id);

    /*查找商品总数量*/
    @Query(nativeQuery = true,value = "select count(*) from test_item ")
    long countProductNum();
}
