package com.ekart.service;

import com.ekart.dto.request.CartDto;
import com.ekart.model.Cart;
import com.ekart.model.CartProduct;
import jakarta.validation.Valid;

public interface CartService {
    int addProductToCart(@Valid CartDto cartDto, String emailId) ;
    String deleteFromCart(@Valid int productId, String emailId);
    String update(int productId, String emailId, int quantity);

    Cart getCart(String emailId);
}
