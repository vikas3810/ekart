package com.ekart.repo;

import com.ekart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Integer> {
    List<Product> findAllByAndIsActive(boolean status);
}
