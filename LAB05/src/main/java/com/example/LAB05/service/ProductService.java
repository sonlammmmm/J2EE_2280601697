package com.example.LAB05.service;

import com.example.LAB05.model.Product;
import com.example.LAB05.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public List<Product> getAll(){
        return productRepository.findAll();
    }

    public void save(Product product){
        productRepository.save(product);
    }

    public Product getById(Integer id){
        return productRepository.findById(id).orElse(null);
    }

    public void update(Product product){
        productRepository.save(product);
    }

    public void delete(Integer id){
        productRepository.deleteById(id);
    }
}