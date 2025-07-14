package com.example.Login.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p")
    ArrayList<Product> findAllProducts();

    @Query("SELECT p FROM Product p " +
            "WHERE p.productName = :productName")
    Optional<Product> findProductByName(String productName);

    @Query("SELECT p FROM Product p " +
            "WHERE p.isAvailable = true")
    ArrayList<Product> findProductsWithAvailableStock();
}
