package com.example.demo.service.impl;

import com.example.demo.model.entity.SizeShoesEntity;
import com.example.demo.repository.SizeRepository;
import com.example.demo.service.SizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SizeServiceImpl implements SizeService {

    private final SizeRepository sizeRepository;


    @Override
    public List<SizeShoesEntity> listSize(){
        return sizeRepository.findAll();
    }
    
}
