package com.example.demo.service.impl;

import com.example.demo.contants.StatusEnum;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.dto.ProductDto;
import com.example.demo.model.entity.CategoryEntity;
import com.example.demo.model.entity.ProductEntity;
import com.example.demo.payload.request.SearchDTO;
import com.example.demo.payload.response.DefaultResponse;
import com.example.demo.payload.response.ServiceResult;
import com.example.demo.repository.BrandRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductCustomRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductCustomRepository productCustomRepository;


    @Override
    public DefaultResponse<ProductDto> viewDetailProduct(Long productId){
        return DefaultResponse.success(productCustomRepository.viewProductDetail(productId));
    }

    @Override
    public ServiceResult<Page<ProductDto>> searchListProduct(SearchDTO<ProductDto> searchDTO){

        List<ProductDto> productDtoList = this.productCustomRepository.searchProductUser(searchDTO);

        int start = searchDTO.getPage() * searchDTO.getPageSize();
        int end = Math.min(start + searchDTO.getPageSize(), productDtoList.size());

        if (start <= end) {
            List<ProductDto> content = productDtoList.subList(start, end);
            Page<ProductDto> userDtoPage = new PageImpl<>(
                    content,
                    PageRequest.of(searchDTO.getPage(), searchDTO.getPageSize()),
                    productDtoList.size()
            );
            return new ServiceResult<>(
                    userDtoPage,
                    HttpStatus.OK,
                    "success")
                    ;
        } else {
            System.out.println("Invalid start and end values.");
            return new ServiceResult<>(
                    new PageImpl<>(Collections.emptyList(), PageRequest.of(searchDTO.getPage(), searchDTO.getPageSize()), 0),
                    HttpStatus.BAD_REQUEST,"Fail"
            );
        }
    }


    @Override
    public ProductDto create(ProductDto productDto) {
        CategoryEntity categoryEntity = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.value(), "Category id not found: " + productDto.getCategoryId()));

        ProductEntity find = productRepository.findAllByCode(productDto.getCode());
        if (find == null) {
            ProductEntity productEntity = modelMapper.map(productDto, ProductEntity.class);

            productEntity.setCategoryId(categoryEntity.getId());
            productEntity.setStatus(StatusEnum.ACTIVE);

            if(productDto.getDiscount() < 1 || productDto.getDiscount() > 100){
                throw new BadRequestException("Mức giảm giá từ 1% - 100%");
            }

            long priceNew = productDto.getPrice() * (100 - productDto.getDiscount()) / 100;
            productEntity.setPriceNew(priceNew);

            return modelMapper.map(productRepository.save(productEntity), ProductDto.class);
        } else {
            throw new BadRequestException("Mã sản phẩm đã tồn tại");
        }
    }

    @Override
    public ProductDto update(Long id, ProductDto productDto) {
        CategoryEntity findCate = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.value(), "Category id not found: " + productDto.getCategoryId()));
        ProductEntity find = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.value(), "Product id not found:" + id));

        ProductEntity productEntity = modelMapper.map(productDto, ProductEntity.class);

        productEntity.setId(find.getId());
        productEntity.setCategoryId(findCate.getId());
        productEntity.setStatus(StatusEnum.ACTIVE);

        if(productDto.getDiscount() < 1 || productDto.getDiscount() > 100){
            throw new BadRequestException("Mức giảm giá từ 1% - 100%");
        }

        long priceNew = productDto.getPrice() * (100 - productDto.getDiscount()) / 100;
        productEntity.setPriceNew(priceNew);

        return modelMapper.map(productRepository.save(productEntity), ProductDto.class);
    }

    @Override
    public void delete(Long id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.value(), "Product id not found:" + id));
        productEntity.setStatus(StatusEnum.DELETED);
        productRepository.save(productEntity);
    }



    @Override
    public ProductEntity getOne(Long productId) {
        ProductEntity findById = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.value(), "Product id not found: " + productId));
        return productRepository.findById(findById.getId()).get();
    }


    @Override
    public ProductEntity updateQuantity(Long productId, int quantity) {
        Optional<ProductEntity> findByIdProduct = productRepository.findById(productId);
        if (findByIdProduct.isPresent()) {
            ProductEntity product = findByIdProduct.get();
            product.setQuantity(quantity);
            return productRepository.save(product);
        }
        return findByIdProduct
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.value(), "Product id not found: " + productId));
    }



}
