package com.ecom.shopping_cart.service;

import com.ecom.shopping_cart.model.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface ProductService {
    public Product saveProduct(Product product);

    public List<Product> getAllProducts();

    public Boolean deleteProduct(Integer id);

    public Product getProductById(Integer id);

    public Product updateProduct(Product product, MultipartFile image, HttpSession session) throws IOException;

    public List<Product> getAllActiveProducts(String category);
}
