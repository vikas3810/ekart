package com.ekart.service;

import com.ekart.dto.request.ProductDto;
import com.ekart.exception.EntityAlreadyExistException;
import com.ekart.model.Category;
import com.ekart.model.Product;
import jakarta.validation.Valid;

import java.util.List;

public interface ProductService {
    int addProduct(@Valid ProductDto productDto,String emailID) throws EntityAlreadyExistException;
    String delete(@Valid int productId);

    String update(int productId, String emailId, ProductDto productDto);

    List<Product> dispAllProduct();
}
