package cn.ff.onlineshop.jpa.vo;

import cn.ff.onlineshop.jpa.entities.Category;
import cn.ff.onlineshop.jpa.entities.Product;

public class ProductVo extends Product {
    private Category category;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
