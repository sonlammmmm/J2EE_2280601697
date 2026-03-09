package com.example.lab04.service;

import com.example.lab04.model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    List<Category> categories = new ArrayList<>();

    public CategoryService(){
        categories.add(new Category(1,"Laptop"));
        categories.add(new Category(2,"Điện thoại"));
    }

    public List<Category> getAll(){
        return categories;
    }

    public Category getById(Integer id){
        return categories.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}