package com.example.demo.service.impl;

import com.example.demo.model.dto.PaymentDto;
import com.example.demo.model.entity.PaymentEntity;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;


    @Override
    public List<PaymentDto> paymentEntityList(){
        List<PaymentEntity> list = paymentRepository.findAll();
        return list.stream().map(paymentEntity -> modelMapper.map(paymentEntity,PaymentDto.class)).collect(Collectors.toList());
    }
}
