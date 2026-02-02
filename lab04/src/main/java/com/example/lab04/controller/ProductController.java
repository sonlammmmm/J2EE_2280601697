package com.example.lab04.controller;

import com.example.lab04.model.Product;
import com.example.lab04.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("listproduct", productService.getAll());
        return "product/products";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAll());
        return "product/create";
    }

    @PostMapping("/create")
    public String create(
            @Valid Product newProduct,
            BindingResult result,
            @RequestParam("category_id") int categoryId,
            @RequestParam("imageProduct") MultipartFile imageProduct,
            Model model
    ) {

        if (result.hasErrors()) {
            model.addAttribute("product", newProduct);
            model.addAttribute("categories", categoryService.getAll());
            return "product/create";
        }

        productService.updateImage(newProduct, imageProduct); // Xử lý ảnh

        Category selectedCategory = categoryService.get(categoryId);
        newProduct.setCategory(selectedCategory);

        productService.add(newProduct);

        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String Edit(@PathVariable int id, Model model) {
        Product find = productService.get(id);

        if (find == null) {
            return "error/404"; // Trang lỗi tuỳ chỉnh
        }

        model.addAttribute("product", find);
        model.addAttribute("categories", categoryService.getAll());
        return "product/edit";
    }

    @PostMapping("/edit")
    public String Edit(
            @Valid Product editProduct,
            BindingResult result,
            @RequestParam("imageProduct") MultipartFile imageProduct,
            Model model
    ) {

        if (result.hasErrors()) {
            model.addAttribute("product", editProduct);
            model.addAttribute("categories", categoryService.getAll());
            return "product/edit";
        }

        if (imageProduct != null && !imageProduct.isEmpty()) {
            productService.updateImage(editProduct, imageProduct); // Cập nhật ảnh nếu có
        }

        productService.update(editProduct); // Cập nhật sản phẩm
        return "redirect:/products";
    }
}
