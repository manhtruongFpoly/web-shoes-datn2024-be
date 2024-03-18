package com.example.demo.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CartDto {
    private Long id;

    private String name;

    private long price;

    private long total;

    private Integer quantity;

    private String image;

    private String sizeName;

    private String colorName;

    @NotNull
    private Long productId;

    private Long userId;
}
