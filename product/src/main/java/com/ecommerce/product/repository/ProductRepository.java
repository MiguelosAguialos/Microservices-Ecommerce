package com.ecommerce.product.repository;

import com.ecommerce.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByIsActiveTrue();

    @Query("SELECT p FROM t_product p WHERE p.isActive = true AND p.stock > 0 AND LOWER(p.name) LIKE LOWER(CONCAT('%', :keyWord, '%'))")
    List<Product> searchProducts(@Param("keyWord") String keyWord);
}
