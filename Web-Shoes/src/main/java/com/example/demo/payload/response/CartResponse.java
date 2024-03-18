package com.example.demo.payload.response;

import lombok.Data;

@Data
public class CartResponse {
    Long totalAmount;
    Integer quantityCart;
}
