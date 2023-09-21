package com.ekart.serviceImpl;

import com.ekart.dto.request.CategoryDto;
import com.ekart.exception.DocumentAlreadyExistsException;
import com.ekart.exception.InvalidCategoryTypeException;
import com.ekart.model.Category;
import com.ekart.model.CategoryType;
import com.ekart.model.User;
import com.ekart.repo.CategoryRepo;
import com.ekart.repo.UserRepo;
import com.ekart.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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

    /**
     * Adds a new category.
     *
     * @param categoryDto The DTO containing category information.
     * @param emailId     The email ID of the user.
     * @return The ID of the newly added category.
     * @throws InvalidCategoryTypeException If the category type is invalid.
     * @throws DocumentAlreadyExistsException If the category type already exists.
     */
    @Override
    public Category addCategory(CategoryDto categoryDto, String emailId)
            throws InvalidCategoryTypeException, DocumentAlreadyExistsException {
        log.info("Inside addCategory method");

        // Retrieve the user entity
        User user = userRepo.findByEmailId(emailId)
                .orElseThrow(ResourceNotFoundException::new);

        // Get the category type from the DTO
        CategoryType categoryType = getCategoryType(categoryDto.getCategoryType().toString());

        // Check if the category type already exists
        checkCategoryTypeDuplication(categoryType);

        // Create a new category
        Category category = Category.builder()
                .categoryType(categoryType)
                .description(categoryDto.getDescription())
                .createdBy(user.getEmailId())
                .createdDate(LocalDateTime.now())
                .modifiedBy(user.getEmailId())
                .modifiedDate(LocalDateTime.now())
                .isActive(true)
                .slug(categoryDto.getSlug())
                .build();

        // Save and return the category
        return  categoryRepo.save(category);

    }

    /**
     * Deletes a category by setting it as inactive.
     *
     * @param categoryId The ID of the category to delete.
     * @return A message indicating that the category was deleted.
     */
    @Override
    public String delete(int categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(ResourceNotFoundException::new);
        category.setActive(false);
        categoryRepo.save(category);
        return "Category deleted";
    }

    /**
     * Updates an existing category.
     *
     * @param categoryId   The ID of the category to update.
     * @param emailId      The email ID of the user performing the update.
     * @param categoryDto  The DTO containing updated category information.
     * @return A message indicating the successful update of the category.
     * @throws InvalidCategoryTypeException If the category type is invalid.
     */
    @Override
    public String update(int categoryId, String emailId, CategoryDto categoryDto)
            throws InvalidCategoryTypeException {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(ResourceNotFoundException::new);

        // Update category information
        category.setCategoryType(getCategoryType(categoryDto.getCategoryType()));
        category.setDescription(categoryDto.getDescription());
        category.setModifiedBy(emailId);
        category.setModifiedDate(LocalDateTime.now());
        category.setSlug(categoryDto.getSlug());

        // Save the updated category
        categoryRepo.save(category);
        return "Category updated successfully";
    }

    /**
     * Retrieves a list of all active categories.
     *
     * @return A list of active categories.
     */
    @Override
    public List<Category> dispAllCategory() {
        return categoryRepo.findAllByIsActive(true);
    }

    /**
     * Checks if a category type already exists in the database.
     *
     * @param categoryType The category type to check.
     * @throws DocumentAlreadyExistsException If the category type already exists.
     */
    public void checkCategoryTypeDuplication(CategoryType categoryType)
            throws DocumentAlreadyExistsException {
        Optional<Category> category = categoryRepo.findByCategoryType(categoryType);
        if (category.isPresent()) {
            throw new DocumentAlreadyExistsException("Category already exists.");
        }
    }

    /**
     * Converts a string to a CategoryType enum.
     *
     * @param type The string representation of the category type.
     * @return The corresponding CategoryType enum.
     * @throws InvalidCategoryTypeException If the category type is invalid.
     */
    public CategoryType getCategoryType(String type)
            throws InvalidCategoryTypeException {
        CategoryType categoryType;
        log.info("getCategoryType ");
        try {
            categoryType = CategoryType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCategoryTypeException("Invalid Category type");
        }
        return categoryType;
    }
}
