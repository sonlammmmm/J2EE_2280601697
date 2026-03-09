package com.example.lab04.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Category {

    private Integer id;

    @NotBlank(message = "Tên danh mục không được để trống")
    private String name;
}