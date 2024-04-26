package com.example.demo.repository;

import com.example.demo.model.entity.ProductColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductColorRepository extends JpaRepository<ProductColor,Long> {
    @Modifying
    @Query(nativeQuery = true, value = "delete from product_color where product_id = :id")
    void deleteAllByProductId(@Param("id") Long id);
}
