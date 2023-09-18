package com.ekart.repo;

import com.ekart.model.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartProductRepo extends JpaRepository<CartProduct,Integer> {
}
