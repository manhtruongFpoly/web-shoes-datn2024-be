package com.example.demo.repository;

import com.example.demo.model.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity,Long> {

    @Query(value = "select * from products p where p.status = 1",nativeQuery = true)
    List<ProductEntity> findByAllProduct();

    ProductEntity findAllByCode(String maSP);

}
