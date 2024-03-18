package com.example.demo.service;

import com.example.demo.payload.response.ServiceResult;

public interface VnPayService {
    ServiceResult<String> payWithVnpay(Long amount);
}
