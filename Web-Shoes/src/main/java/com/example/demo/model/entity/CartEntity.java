package com.example.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "carts")
@Data
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private long price;
    private Long total;
    private Integer quantity;
    private String image;
    private String sizeName;
    private String colorName;
    @JsonProperty("product_id")
    private Long productId;
    @JsonProperty("user_id")
    private Long userId;

}
