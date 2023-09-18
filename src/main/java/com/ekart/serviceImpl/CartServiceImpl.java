package com.ekart.serviceImpl;

import com.ekart.dto.request.CategoryDto;
import com.ekart.dto.request.ProductDto;
import com.ekart.exception.DocumentAlreadyExistsException;
import com.ekart.exception.InvalidCategoryTypeException;
import com.ekart.model.Category;
import com.ekart.model.CategoryType;
import com.ekart.model.Product;
import com.ekart.model.User;
import com.ekart.repo.CategoryRepo;
import com.ekart.repo.UserRepo;
import com.ekart.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Component
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepo categoryRepo;
    private final UserRepo userRepo;
    @Override
    public int addCategory(CategoryDto categoryDto, String emailId) throws InvalidCategoryTypeException, DocumentAlreadyExistsException {
        log.info("Inside addCategory method");
        User user = userRepo.findByEmailId(emailId).orElseThrow(ResourceNotFoundException::new);
        CategoryType categoryType = getCategoryType(categoryDto.getCategoryType().toString());
        checkCategoryTypeDuplication(categoryType);
        Category category = Category.builder()
                .categoryType(categoryDto.getCategoryType())
                .description(categoryDto.getDescription())
                .createdBy(user.getEmailId())
                .createdDate(LocalDateTime.now())
                .modifiedBy(user.getEmailId())
                .modifiedDate(LocalDateTime.now())
                .isActive(true)
                .slug(categoryDto.getSlug())
                .build();
        Category saveCategory = categoryRepo.save(category);
        return saveCategory.getCategoryId();
    }

    @Override
    public String delete(int categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(ResourceNotFoundException::new);
        category.setActive(false);
        categoryRepo.save(category);
        return "Category deleted";
    }

    @Override
    public String update(int categoryId, String emailId, CategoryDto categoryDto) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(ResourceNotFoundException::new);
        category.setCategoryType(categoryDto.getCategoryType());
        category.setDescription(categoryDto.getDescription());
        category.setModifiedBy(emailId);
        category.setModifiedDate(LocalDateTime.now());
        category.setSlug(categoryDto.getSlug());

        categoryRepo.save(category);
        return "Category updated successfully";
    }

    @Override
    public List<Category> dispAllCategory() {
        return categoryRepo.findAllByIsActive(true);
    }

    public void checkCategoryTypeDuplication(CategoryType categoryType) throws DocumentAlreadyExistsException {
        Optional<Category> category =
                categoryRepo.findByCategoryType(categoryType);
        if (category.isPresent()) {
            throw new DocumentAlreadyExistsException("Category already exist.");
        }
    }
    public CategoryType getCategoryType(String type) throws InvalidCategoryTypeException {
        CategoryType categoryType;
        try {
            categoryType = CategoryType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCategoryTypeException("Invalid Category type");
        }
        return categoryType;
    }
}
