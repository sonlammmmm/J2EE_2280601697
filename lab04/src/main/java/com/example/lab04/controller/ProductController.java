package com.example.lab04.controller;

import com.example.lab04.model.Product;
import com.example.lab04.service.ProductService;
import com.example.lab04.service.CategoryService;

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
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    private final String uploadDir = "src/main/resources/static/images/";

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
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path path = Paths.get(uploadDir + fileName);
                Files.copy(file.getInputStream(), path);
                product.setImage(fileName);
            } catch (IOException e) {
                e.printStackTrace();
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
                       Model model){

        if(result.hasErrors()){
            model.addAttribute("categories", categoryService.getAll());
            return "product/edit";
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

}