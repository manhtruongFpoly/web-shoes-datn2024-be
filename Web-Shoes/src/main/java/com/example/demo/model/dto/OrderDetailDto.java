package com.example.demo.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@Data
public class OrderDetailDto {

    private Long id;

    private Long productPrice;

    private String productName;

    private Integer quantity;

    private Long total;

    private String image;

    private Long productId;

    private Long orderId;

    private Long userId;

    private String username;

    private String sizeName;

    private String colorName;

    public OrderDetailDto(Long id, Long productPrice,
                          String productName, Integer quantity,
                          Long total, String image, Long productId,
                          Long orderId, Long userId, String username,
                          String sizeName, String colorName
    ) {
        this.id = id;
        this.productPrice = productPrice;
        this.productName = productName;
        this.quantity = quantity;
        this.total = total;
        this.image = image;
        this.productId = productId;
        this.orderId = orderId;
        this.userId = userId;
        this.username = username;
        this.sizeName = sizeName;
        this.colorName = colorName;
    }
}
