package cn.ff.onlineshop.service.impl;

import cn.ff.onlineshop.jpa.entities.Category;
import cn.ff.onlineshop.jpa.reposity.CategoryRepository;
import cn.ff.onlineshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Category> findAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category c) {
        return categoryRepository.save(c);
    }

    @Override
    public void deleteCategory(String cid) {
        categoryRepository.delete(cid);
    }


    @Override
    public void updateCategory(Category c) {
        categoryRepository.save(c);
    }

    @Override
    public Category findCategoryByCid(String cid) {
        return categoryRepository.findOne(cid);
    }

    @Override
    public Category findCategoryByCname(String cname) {
        return categoryRepository.findByCname(cname);
    }
}
