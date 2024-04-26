package com.example.demo.model.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductDto {
    private Long id;

    @NotBlank(message = "code is not null")
    private String code;

    @NotBlank(message = "product name is not null")
    @Size(max = 244)
    private String name;

    @DecimalMin(value = "0.0", inclusive = false)
    private long price;

    private long priceNew;

    @NotNull(message = "quantity not is null")
    @Min(value = 0, message = "quantity min is 0")
    private Integer quantity;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Integer discount;
    private String description;
    private Integer status;
    private Long categoryId;
    private Long brandId;
    private String keySearch;
    private String listSizes;
    private String listColors;
    private List<MultipartFile> files = new ArrayList<>();
    private String sizeName;
    private String colorName;
    private Long idOrder;
    private String imgList;
    private String[] listImgDelete = new String[0];
}
