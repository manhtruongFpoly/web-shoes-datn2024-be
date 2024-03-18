package com.example.demo.service.impl;

import com.example.demo.model.entity.BrandEntity;
import com.example.demo.repository.BrandRepository;
import com.example.demo.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    @Override
    public List<BrandEntity> getAll() {
        return brandRepository.findAll();
    }

}
