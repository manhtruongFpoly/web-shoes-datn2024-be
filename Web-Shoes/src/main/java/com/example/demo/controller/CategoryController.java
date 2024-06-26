package com.example.demo.controller;

import com.example.demo.payload.response.DefaultResponse;
import com.example.demo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    //todo:Danh sách category theo status
    @GetMapping("/list-status")
    public ResponseEntity<?> listCategory(
    ) {
        return ResponseEntity.ok(DefaultResponse.success(categoryService.listCategory()));
    }



}
