package com.ekart.service;

import com.ekart.dto.request.CartDto;
import com.ekart.model.Cart;
import jakarta.validation.Valid;

public interface OrderService {
  String orderNow(String emailId);
  String cancelOrder(String emailId,int orderId);
}
