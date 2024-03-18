package com.example.demo.repository;

import com.example.demo.model.dto.ProductDto;
import com.example.demo.payload.request.SearchDTO;

import java.util.List;

public interface ProductCustomRepository {
    List<ProductDto> searchProductUser(SearchDTO<ProductDto> searchDTO);

    ProductDto viewProductDetail(Long productId);
}
