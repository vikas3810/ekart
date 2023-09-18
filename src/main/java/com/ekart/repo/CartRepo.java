package com.ekart.repo;

import com.ekart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart,Integer> {


    Optional<Cart> findByUserUserId(Integer userId);
}
