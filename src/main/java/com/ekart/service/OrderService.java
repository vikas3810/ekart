package com.ekart.service;

import com.ekart.dto.request.CartDto;
import com.ekart.model.Cart;
import com.ekart.model.Orders;
import jakarta.validation.Valid;

import java.util.List;

public interface OrderService {
    String orderNow(String emailId, int addressId);

    String cancelOrder(String emailId, int orderId);

    List<Orders> getAllOrder(String emailId);
}
