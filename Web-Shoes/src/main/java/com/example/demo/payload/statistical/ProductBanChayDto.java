package com.example.demo.payload.statistical;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductBanChayDto {

    private Long id;
    private String productName;
    private String code;
    private Long quantity;
    private Long total;
}
