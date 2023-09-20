package com.ekart.repo;

import com.ekart.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Orders, Integer> {
    List<Orders> findByUserUserId(int userId);
}
