package cn.ff.onlineshop.service.impl;

import cn.ff.onlineshop.jpa.entities.Category;
import cn.ff.onlineshop.jpa.entities.Ordered;
import cn.ff.onlineshop.jpa.entities.Product;
import cn.ff.onlineshop.jpa.reposity.CategoryRepository;
import cn.ff.onlineshop.jpa.reposity.OrderItemRepository;
import cn.ff.onlineshop.jpa.reposity.OrderedRepository;
import cn.ff.onlineshop.jpa.reposity.ProductRepository;
import cn.ff.onlineshop.jpa.vo.OrderItemInfoVo;
import cn.ff.onlineshop.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderedRepository orderedRepository;
    @Autowired
    OrderItemRepository orderItemRepository;


    @Override
    public Product saveProduct(Product product)  {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(String pid) {
        productRepository.delete(pid);
    }

    @Override
    public long countProductNum() {
        return productRepository.countProductNum();
    }

    @Override
    public  Page<Product> findAllProduct(int pagenum, int pagesize) {
        PageRequest pageable = new PageRequest(pagenum, pagesize, Sort.Direction.DESC, "pdate");
        return productRepository.findProductsInPage(pageable);
    }

    @Override
    public List<Ordered> findAllOrders() {
        return orderedRepository.findAll();
    }

    @Override
    public List<OrderItemInfoVo> findOrderInfoByOid(String oid) {
        return orderItemRepository.findByOrderid(oid);
    }

    @Override
    public Page<Product> findProductByKey(int pagenum, int pagesize, String key, String type) {
        PageRequest pageable = new PageRequest(pagenum, pagesize, Sort.Direction.DESC, "pdate");

        if(type.equals("0")){//根据id查找

            return productRepository.findByPidLike(key,pageable);
        }else if(type.equals("1")){//根据名称查找

            return productRepository.findByNameLike(key,pageable);
        }else if(type.equals("2")){//根据分类名称查找

            Category category = categoryRepository.findByCname(key);
            String cid = category.getCid();
            return productRepository.findByCid(cid,pageable);
        }

        return null;
    }
}
