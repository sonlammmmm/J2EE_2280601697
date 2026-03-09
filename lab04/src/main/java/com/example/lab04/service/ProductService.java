package com.example.lab04.service;

import com.example.lab04.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    List<Product> products = new ArrayList<>();

    public List<Product> getAll(){
        return products;
    }

    public Product getById(Integer id){
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void save(Product product){
        product.setId(products.size()+1);
        products.add(product);
    }

    public void update(Product product){
        Product p = getById(product.getId());

        if(p != null){
            p.setName(product.getName());
            p.setPrice(product.getPrice());
            p.setCategory(product.getCategory());
            p.setImage(product.getImage());
        }
    }

    public void delete(Integer id){
        products.removeIf(p -> p.getId().equals(id));
    }
}