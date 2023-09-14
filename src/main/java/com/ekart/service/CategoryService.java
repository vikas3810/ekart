package com.ekart.service;

import com.ekart.dto.request.CategoryDto;
import com.ekart.dto.request.ProductDto;
import com.ekart.exception.DocumentAlreadyExistsException;
import com.ekart.exception.EntityAlreadyExistException;
import com.ekart.exception.InvalidCategoryTypeException;
import com.ekart.model.Category;
import jakarta.validation.Valid;

import java.util.List;

public interface CategoryService {
    int addCategory(@Valid CategoryDto categoryDto, String emailId) throws EntityAlreadyExistException, InvalidCategoryTypeException, DocumentAlreadyExistsException;
    String delete(@Valid int categoryId);

    String update(int categoryId, String emailId, CategoryDto categoryDto);

    List<Category> dispAllCategory();
}
