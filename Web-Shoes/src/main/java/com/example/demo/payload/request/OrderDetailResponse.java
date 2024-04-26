package com.example.demo.payload.request;

import lombok.Data;

@Data
public class OrderDetailResponse {
    private Long totalAmount;
    private Integer shipping;
//    private Integer quantity;
}
