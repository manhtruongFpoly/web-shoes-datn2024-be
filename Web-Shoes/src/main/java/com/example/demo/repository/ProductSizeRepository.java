package com.example.demo.repository;

import com.example.demo.model.entity.ProductSizeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSizeRepository extends JpaRepository<ProductSizeEntity,Long> {
    @Modifying
    @Query(nativeQuery = true, value = "delete from product_sizes where product_id = :id")
    void deleteAllByProductId(@Param("id") Long id);
}
