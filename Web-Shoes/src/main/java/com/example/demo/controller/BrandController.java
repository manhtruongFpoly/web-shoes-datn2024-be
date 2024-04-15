package com.example.demo.controller;

import com.example.demo.payload.response.DefaultResponse;
import com.example.demo.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;


    @GetMapping("/list-brand")
    public ResponseEntity<?> listBrand(
    ) {
        return ResponseEntity.ok(DefaultResponse.success(brandService.getAll()));
    }


}
