package com.example.demo.service;

import com.example.demo.model.dto.ProductDto;
import com.example.demo.model.entity.ProductEntity;
import com.example.demo.payload.request.SearchDTO;
import com.example.demo.payload.response.DefaultResponse;
import com.example.demo.payload.response.ServiceResult;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    DefaultResponse<ProductDto> viewDetailProduct(Long productId);

    ServiceResult<Page<ProductDto>> searchListProduct(SearchDTO<ProductDto> searchDTO);

    ProductDto create(ProductDto productDto);

    ProductDto update(Long id, ProductDto productDto);

    void delete(Long id);

    ProductEntity getOne(Long productId);

    ProductEntity updateQuantity(Long productId, int quantity);
}
