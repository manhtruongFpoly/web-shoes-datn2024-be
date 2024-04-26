package com.example.demo.controller;

import com.example.demo.service.VnPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/vnPay")
@RequiredArgsConstructor
public class VnPayController {

    private final VnPayService vnPayService;

    @GetMapping("/payment")
    public ResponseEntity<?> payWithVnpay(
            @RequestParam("amount") Long amount
    ){
        return ResponseEntity.ok().body(this.vnPayService.payWithVnpay(amount));
    }

}
