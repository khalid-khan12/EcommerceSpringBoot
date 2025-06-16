package com.ecom.shopping_cart.controller;

import com.ecom.shopping_cart.model.Category;
import com.ecom.shopping_cart.model.Product;
import com.ecom.shopping_cart.model.UserDetails;
import com.ecom.shopping_cart.service.CategoryService;
import com.ecom.shopping_cart.service.ProductService;
import com.ecom.shopping_cart.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

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

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute UserDetails user, @RequestParam("img") MultipartFile file, HttpSession session) throws IOException {
        String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
        user.setProfileImage(imageName);
        UserDetails savedUser = userService.saveUser(user);
        userService.saveUser(user);

        if(!ObjectUtils.isEmpty(savedUser))
        {
            if(!file.isEmpty())
            {
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                session.setAttribute("succMsg", "profile added successfully");
            } else {
                session.setAttribute("errorMsg", "Error adding profile");
            }
        }

        return "redirect:/register";
    }
}
