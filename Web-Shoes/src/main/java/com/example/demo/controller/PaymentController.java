package com.example.demo.controller;

import com.example.demo.payload.response.DefaultResponse;
import com.example.demo.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    //todo:Get all payment
    @GetMapping("/list")
    public ResponseEntity<?> getAll(
    ) {
        return ResponseEntity.ok(
                DefaultResponse.success(paymentService.paymentEntityList()));
    }

}
