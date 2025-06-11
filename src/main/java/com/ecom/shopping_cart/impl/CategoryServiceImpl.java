package com.ecom.shopping_cart.impl;

import com.ecom.shopping_cart.model.Category;
import com.ecom.shopping_cart.repository.CategoryRepository;
import com.ecom.shopping_cart.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category saveCategory(Category category) {
        return  categoryRepository.save(category);
    }

    @Override
    public boolean existCategory(String name) {
        return categoryRepository.existsByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
