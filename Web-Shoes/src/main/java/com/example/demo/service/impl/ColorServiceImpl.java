package com.example.demo.service.impl;

import com.example.demo.model.entity.ColorEntity;
import com.example.demo.repository.ColorRepository;
import com.example.demo.service.ColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorServiceImpl implements ColorService {

    private final ColorRepository colorRepository;

    @Override
    public List<ColorEntity> listColor(){
        return colorRepository.findAll();
    }
    
}
