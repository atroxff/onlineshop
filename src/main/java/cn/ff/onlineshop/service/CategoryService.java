package cn.ff.onlineshop.service;

import cn.ff.onlineshop.jpa.entities.Category;

import java.util.List;

public interface CategoryService {


    /*获得所有分类*/
    List<Category> findAllCategory();

    /*添加分类*/
    Category addCategory(Category c);

    /*删除分类*/
    void deleteCategory(String cid);

    /*修改分类*/
    void updateCategory(Category category);

    /*根据名称查找分类*/
    Category findCategoryByCname(String cname);

    /*根据名称查找分类*/
    Category findCategoryByCid(String cid);
}
