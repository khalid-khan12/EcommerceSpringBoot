package com.ecom.shopping_cart.service;

import java.util.List;
import com.ecom.shopping_cart.model.Category;

public interface CategoryService {
    public Category saveCategory(Category category);

    public boolean existCategory(String name);

    public List<Category> getAllCategories();

    public boolean deleteCategory(int id);

    public Category getCategoryById(int id);
}
