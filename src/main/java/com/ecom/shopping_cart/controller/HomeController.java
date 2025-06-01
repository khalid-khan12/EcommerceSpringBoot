package com.ecom.shopping_cart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index(){
        System.out.println("index");
        return "index.html";
    }

    @GetMapping("/login")
    public String login(){
        return "login.html";
    }

    @GetMapping("/register")
    public String register(){
        return "register.html";
    }

    @GetMapping("/products")
    public String products(){
        return "products.html";
    }

    @GetMapping("/view_product")
    public String view_product(){ return "view_product.html"; }
}
