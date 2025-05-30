package com.ib.productservice.repository;

import com.ib.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    boolean existsProductBySKU(String sku);

    boolean existsProductById(int id);

    List<Product> findAllByActive(boolean active);

    Optional<Product> findByIdAndActive(int id, boolean active);
}
