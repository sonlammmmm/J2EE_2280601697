package com.example.lab04.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.lab04.model.Category;

@Service
public class CategoryService {

	private final List<Category> listCategory = new ArrayList<>();

	public CategoryService() {
		listCategory.add(new Category(1, "Đồ điện tử"));
		listCategory.add(new Category(2, "Sách"));
		listCategory.add(new Category(3, "Quần áo"));
        listCategory.add(new Category(4, "Giày dép"));
        listCategory.add(new Category(5, "Điện thoại"));
        listCategory.add(new Category(6, "Máy tính"));
	}

	public List<Category> getAll() {
		return listCategory;
	}

	public Category get(int id) {
		return listCategory.stream()
				.filter(c -> c.getId() == id)
				.findFirst()
				.orElse(null);
	}

	public void add(Category category) {
		int maxId = listCategory.stream().mapToInt(Category::getId).max().orElse(0);
		category.setId(maxId + 1);
		listCategory.add(category);
	}
}
