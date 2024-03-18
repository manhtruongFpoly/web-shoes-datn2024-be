package com.example.demo.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.WrongFomatException;
import com.example.demo.model.dto.ProductDto;
import com.example.demo.model.entity.ProductColor;
import com.example.demo.model.entity.ProductEntity;
import com.example.demo.model.entity.ProductSizeEntity;
import com.example.demo.repository.ProductColorRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ProductSizeRepository;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Service
@Transactional
public class CloudinaryServiceImpl {

    private final Cloudinary cloudinary;

    private final ModelMapper modelMapper;

    private final ProductRepository productRepository;

    private final ProductColorRepository productColorRepository;

    private final ProductSizeRepository productSizeRepository;

    public String uploadImage(MultipartFile file, String folder) {
        try {
            if (Objects.requireNonNull(file.getContentType()).contains("image")) {
                Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                        "folder", folder
                ));
                return this.getUrlImage(uploadResult.get("public_id").toString());
            }else{
                throw new WrongFomatException("file is not an image");
            }
        }catch (IOException e){
            throw new NotFoundException(HttpStatus.NOT_FOUND.value(), "File not found");
        }
    }

    public List<String> uploadImages(ProductDto productDto, String folder) {
        List<String> urls = new ArrayList<>();

        for (MultipartFile file : productDto.getFiles()) {
            try {
                if (Objects.requireNonNull(file.getContentType()).contains("image")) {
                    Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                            "folder", folder
                    ));
                    urls.add(this.getUrlImage(uploadResult.get("public_id").toString()));
                } else {
                    throw new WrongFomatException("File is not an image");
                }
            } catch (IOException e) {
                throw new NotFoundException(HttpStatus.NOT_FOUND.value(), "File not found");
            }
        }

        ProductEntity productEntity = modelMapper.map(productDto, ProductEntity.class);
        productEntity.setImgList(String.join(",", urls));
        productRepository.save(productEntity);

        List<ProductColor> productColors = Stream.of(productDto.getListColors().split(",")).map(str -> new ProductColor(null, productEntity.getId(), Long.valueOf(str))).collect(Collectors.toList());
        productColorRepository.saveAll(productColors);

        List<ProductSizeEntity> productSizeEntities = Stream.of(productDto.getListSizes().split(",")).map(str -> new ProductSizeEntity(null, productEntity.getId(), Long.valueOf(str))).collect(Collectors.toList());
        productSizeRepository.saveAll(productSizeEntities);

        return urls;
    }

    public String getUrlImage(String publicId) {
        System.out.println(cloudinary.url().generate(publicId) + "get url image");
        return cloudinary.url().generate(publicId);
    }


}
