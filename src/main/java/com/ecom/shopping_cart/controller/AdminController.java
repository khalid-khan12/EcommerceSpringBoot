package com.ecom.shopping_cart.controller;

import com.ecom.shopping_cart.model.Category;
import com.ecom.shopping_cart.model.Product;
import com.ecom.shopping_cart.repository.ProductRepository;
import com.ecom.shopping_cart.service.CategoryService;
import com.ecom.shopping_cart.service.ProductService;
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
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/")
    public String index() {
        return "admin/index";   // no leading slash, no extension
    }

    @GetMapping("/loadAddProduct")
    public String loadAddProduct(Model m) {
        List<Category> categories = categoryService.getAllCategories();
        m.addAttribute("categories", categories);
        return "admin/add_product";  // remove .html
    }

    @GetMapping("/category")
    public String category(Model m) {
        m.addAttribute("categorys", categoryService.getAllCategories());
        return "admin/category";  // remove .html
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
        String imageName = file != null ? file.getOriginalFilename() : "default.jpg";
        category.setImageName(imageName);
        if(categoryService.existCategory(category.getName())) {
            session.setAttribute("errorMsg", "category already exist");
        } else {
            Category savedCategory = categoryService.saveCategory(category);
            if (ObjectUtils.isEmpty(savedCategory)) {
                session.setAttribute("errorMsg", "Internal server error");
            } else {
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                session.setAttribute("succMsg", "category saved successfully");

            }
        }
        return "redirect:/admin/category";
    }

    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable int id, HttpSession session) {
        boolean deleted = categoryService.deleteCategory(id);
        if(deleted) {
            session.setAttribute("succMsg", "category deleted successfully");
        } else {
            session.setAttribute("errorMsg", "Internal server error");
        }
        return "redirect:/admin/category";
    }

    @GetMapping("/loadEditCategory/{id}")
    public String loadEditCategory(@PathVariable int id, Model m){
        m.addAttribute("category", categoryService.getCategoryById(id));
        return "admin/edit_category";
    }

    @PostMapping("/updateCategory")
    public String updateCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
        Category oldCategory = categoryService.getCategoryById(category.getId());
        String imageName = file.isEmpty() ? oldCategory.getImageName() : file.getOriginalFilename();

        if(!ObjectUtils.isEmpty(category)) {
            oldCategory.setName(category.getName());
            oldCategory.setActive(category.isActive());
            oldCategory.setImageName(imageName);
        }

        Category updatedCategory = categoryService.saveCategory(oldCategory);

        if(!ObjectUtils.isEmpty(updatedCategory)) {
            if(!file.isEmpty()) {
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            session.setAttribute("succMsg", "category updated successfully");
        } else {
            session.setAttribute("errorMsg", "Internal server error");
        }
        return "redirect:/admin/loadEditCategory/" + category.getId();
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image,HttpSession session) throws IOException {
        String imageName = image.isEmpty()? "default.jpg" : image.getOriginalFilename();

        product.setImage(imageName);

        Product savedProduct = productService.saveProduct(product);

        if(!ObjectUtils.isEmpty(savedProduct)) {
            if(!image.isEmpty()) {
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator + image.getOriginalFilename());
                System.out.println(path.toString());
                Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            session.setAttribute("succMsg", "product saved successfully");
        }  else {
            session.setAttribute("errorMsg", "Internal server error");
        }
        return "redirect:/admin/loadAddProduct";
    }

    @GetMapping("products")
    public String loadViewProducts(Model m) {
        m.addAttribute("products", productService.getAllProducts());
        return "admin/products";
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable int id, HttpSession session) {
        Boolean isDeleted = productService.deleteProduct(id);
        if(isDeleted) {
            session.setAttribute("succMsg", "product deleted successfully");
        } else {
            session.setAttribute("errorMsg", "Internal server error");
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/editProduct/{id}")
    public String loadEditProduct(@PathVariable int id, Model m) {
        m.addAttribute("product", productService.getProductById(id));
        m.addAttribute("categories", categoryService.getAllCategories());
        return "admin/edit_product";
    }

    @PostMapping("/updateProduct")
    public String updateProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image, HttpSession session) throws IOException {
        Product dbProduct = productService.getProductById(product.getId());

        String imageName = image.isEmpty() ? dbProduct.getImage() : image.getOriginalFilename();
        dbProduct.setTitle(product.getTitle());
        dbProduct.setPrice(product.getPrice());
        dbProduct.setDescription(product.getDescription());
        dbProduct.setStock(product.getStock());
        dbProduct.setCategory(product.getCategory());

        Product updatedProduct = productRepository.save(dbProduct);

        if(!ObjectUtils.isEmpty(updatedProduct)) {
            if(imageName != null) {
                try {
                    File saveFile = new ClassPathResource("static/img").getFile();
                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator + image.getOriginalFilename());
                    System.out.println(path.toString());
                    Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    session.setAttribute("succMsg", "product updated successfully");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                session.setAttribute("errorMsg", "Internal server error");
            }
        }

        return "redirect:/admin/editProduct/" + product.getId();
    }
}
