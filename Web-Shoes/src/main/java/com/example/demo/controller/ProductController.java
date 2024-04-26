package com.example.demo.controller;

import com.example.demo.contants.FolderContants;
import com.example.demo.model.dto.ProductDto;
import com.example.demo.payload.request.SearchDTO;
import com.example.demo.payload.response.DefaultResponse;
import com.example.demo.payload.response.SampleResponse;
import com.example.demo.service.ProductService;
import com.example.demo.service.impl.CloudinaryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
    public class ProductController {

    private final ProductService productService;
    private final CloudinaryServiceImpl cloudinaryService;

    @GetMapping("/product-id/{id}")
    public ResponseEntity<?> getByIdProduct(
            @PathVariable("id") Long cid
    ) {
        return ResponseEntity.ok(
                DefaultResponse.success(productService.viewDetailProduct(cid)));
    }

    //todo:Lấy tất cả danh sách san phẩm"
    @PostMapping("/search")
    public ResponseEntity<?> searchListProduct(
            @RequestBody SearchDTO<ProductDto> searchDTO
    ){
        return ResponseEntity.ok().body(productService.searchListProduct(searchDTO));
    }


    @PutMapping("/update/product/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ProductDto productDto) {
        SampleResponse response = SampleResponse.builder()
                .success(true)
                .message("Update success")
                .data(productService.update(id, productDto))
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create/product")
    public ResponseEntity<?> create(@Valid @RequestBody ProductDto productDto) {
        SampleResponse response = SampleResponse.builder()
                .success(true)
                .message("Create success")
                .data(productService.create(productDto))
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        productService.delete(id);
        return ResponseEntity.ok(DefaultResponse.success("Delete success"));
    }


    @PostMapping("/upload-images")
    public ResponseEntity<?> create(
            @RequestParam(required = false) MultipartFile file
    ) {
        return ResponseEntity.ok(
                DefaultResponse.success(cloudinaryService.uploadImage(file, FolderContants.PRODUCTS_IMAGES_FOLDER)));
    }

    @PostMapping("/upload-list-images")
    public ResponseEntity<?> create1(ProductDto productDto) {
        return ResponseEntity.ok(
                DefaultResponse.success(cloudinaryService.uploadImages(productDto, FolderContants.PRODUCTS_IMAGES_FOLDER)));
    }

    @GetMapping("/get-one/{id}")
    public ResponseEntity<?> getOne(
            @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(DefaultResponse.success(productService.getOne(id)));
    }


}
