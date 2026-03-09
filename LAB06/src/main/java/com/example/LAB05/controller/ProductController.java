package com.example.LAB05.controller;

import com.example.LAB05.model.Product;
import com.example.LAB05.service.ProductService;
import com.example.LAB05.service.CategoryService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
@RequestMapping("/products")

public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;


    @GetMapping
    public String index(Model model){

        model.addAttribute("products", productService.getAll());

        return "product/products";
    }


    @GetMapping("/create")
    public String create(Model model){

        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAll());

        return "product/create";
    }


    @PostMapping("/create")
    public String create(@Valid Product product,
                         BindingResult result,
                         @RequestParam("imageProduct") MultipartFile file,
                         Model model){

        if(result.hasErrors()){
            model.addAttribute("categories", categoryService.getAll());
            return "product/create";
        }

        if(!file.isEmpty()){
            try {
                product.setImage(storeImage(file));
            } catch (IOException e) {
                model.addAttribute("categories", categoryService.getAll());
                model.addAttribute("uploadError", "Không thể lưu ảnh sản phẩm. Vui lòng thử lại.");
                return "product/create";
            }
        }

        // gán category theo id
        if(product.getCategory() != null){
            product.setCategory(
                categoryService.getById(product.getCategory().getId())
            );
        }

        productService.save(product);

        return "redirect:/products";
    }


    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){

        Product product = productService.getById(id);

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAll());

        return "product/edit";
    }


    @PostMapping("/edit")
    public String edit(@Valid Product product,
                       BindingResult result,
                       @RequestParam("imageProduct") MultipartFile file,
                       Model model){

        if(result.hasErrors()){
            model.addAttribute("categories", categoryService.getAll());
            return "product/edit";
        }

        if(!file.isEmpty()){
            try {
                product.setImage(storeImage(file));
            } catch (IOException e) {
                model.addAttribute("categories", categoryService.getAll());
                model.addAttribute("uploadError", "Không thể cập nhật ảnh sản phẩm. Vui lòng thử lại.");
                return "product/edit";
            }
        }

        if(product.getCategory() != null){
            product.setCategory(
                categoryService.getById(product.getCategory().getId())
            );
        }

        productService.update(product);

        return "redirect:/products";
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){

        productService.delete(id);

        return "redirect:/products";
    }

    private String storeImage(MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();
        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf('.'));
        }

        String generatedName = UUID.randomUUID() + extension;
        Path uploadDir = Paths.get("uploads", "images");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        Path destination = uploadDir.resolve(generatedName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        return generatedName;
    }

}
