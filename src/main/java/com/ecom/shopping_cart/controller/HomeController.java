package com.ecom.shopping_cart.controller;

import com.ecom.shopping_cart.model.Category;
import com.ecom.shopping_cart.model.Product;
import com.ecom.shopping_cart.service.CategoryService;
import com.ecom.shopping_cart.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @GetMapping("/products")
    public String products(Model model, @RequestParam(value="category", defaultValue = "") String category){
        List<Category> activeCategories = categoryService.getAllActiveCategories();
        List<Product> activeProducts = productService.getAllActiveProducts(category);
        model.addAttribute("categories", activeCategories);
        model.addAttribute("products", activeProducts);
        model.addAttribute("paramValue", category);
        return "products";
    }

    @GetMapping("/product/{id}")
    public String view_product(@PathVariable int id, Model model){
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "view_product.html";
    }
}
