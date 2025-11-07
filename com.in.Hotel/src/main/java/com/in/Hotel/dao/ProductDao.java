package com.in.Hotel.dao;

import com.in.Hotel.POJO.Product;
import com.in.Hotel.wrapper.ProductWrapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductDao extends JpaRepository<Product, Integer> {

    List<ProductWrapper> getAllProduct();

    @Modifying
    @Transactional
    Integer updateProductStatus(@Param("status") String status, @Param("id") Integer id);

    List<ProductWrapper> getByCategory(@Param("categoryId") Integer categoryId);

    ProductWrapper getProductById(@Param("id") Integer id);
}