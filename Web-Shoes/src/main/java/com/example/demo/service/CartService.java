package com.example.demo.service;

import com.example.demo.model.dto.CartDto;
import com.example.demo.model.entity.CartEntity;
import com.example.demo.payload.response.CartResponse;

import java.util.Collection;

public interface CartService {
    Collection<CartEntity> getAllByUser(Long id);

    Collection<CartEntity> getAllCart();

    CartEntity getById(Long id);

    CartResponse sumTotalPriceAndQuantity(Long id);

    CartDto addToCart(CartDto cartDto);

    CartDto updateQuantity(Long idProduct, Integer quantity);

    void delete(Long id);

    void deleteAll();
}
