package com.example.LAB05.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.LAB05.model.Product;

public interface ProductRepository extends JpaRepository<Product,Integer> {

}