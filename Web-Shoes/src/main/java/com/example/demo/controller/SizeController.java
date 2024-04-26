package com.example.demo.controller;

import com.example.demo.service.SizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/size")
@RequiredArgsConstructor
public class SizeController {

    private final SizeService sizeService;

    @GetMapping("/list")
    public ResponseEntity<?> getAllSize(){
        return ResponseEntity.ok().body(sizeService.listSize());
    }


}
