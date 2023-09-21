package com.ekart.controller;

import com.ekart.dto.request.CategoryDto;
import com.ekart.dto.response.ApiResponse;
import com.ekart.exception.DocumentAlreadyExistsException;
import com.ekart.exception.EntityAlreadyExistException;
import com.ekart.exception.InvalidCategoryTypeException;
import com.ekart.exception.UnAuthorizedUserException;
import com.ekart.jwt.JwtService;
import com.ekart.model.Category;
import com.ekart.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ekart.util.AuthenticationHelper.*;

@RestController
@RequestMapping("/category")
@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final JwtService jwtService;

    /**
     * Add a new category.
     *
     * @param categoryDto The category DTO.
     * @param request     The HTTP request.
     * @return ResponseEntity with ApiResponse indicating success or failure.
     * @throws EntityAlreadyExistException  if the category already exists.
     * @throws InvalidCategoryTypeException if the category type is invalid.
     * @throws UnAuthorizedUserException    if the user is not authorized.
     * @throws DocumentAlreadyExistsException if the document already exists.
     */
    @PostMapping("/addCategory")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody @Valid CategoryDto categoryDto,
                                                   HttpServletRequest request
    ) throws EntityAlreadyExistException, InvalidCategoryTypeException, UnAuthorizedUserException, DocumentAlreadyExistsException {
        log.info("addCategory called in CategoryController controller ");
        // Get email from JWT(request)
        String emailId = getEmailFromJwt(request);

        // Check if emailId belongs to an admin
        adminAuthorized(emailId);

        // Check authorization
        compareJwtEmailIdAndCustomerEmailId(request, jwtService, emailId);

        Category category = categoryService.addCategory(categoryDto, emailId);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.CREATED.value(), "Category is created successfully", category);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Delete a category by categoryId.
     *
     * @param categoryId The categoryId to delete.
     * @param request    The HTTP request.
     * @return ResponseEntity with ApiResponse indicating success or failure.
     * @throws UnAuthorizedUserException if the user is not authorized.
     */
    @PostMapping("/{categoryId}/deleteCategory")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable int categoryId,
                                                      HttpServletRequest request
    ) throws UnAuthorizedUserException {
        log.info("inside deleteCategory method controller");
        // Get email from JWT(request)
        String emailId = getEmailFromJwt(request);

        // Check if emailId belongs to an admin
        adminAuthorized(emailId);

        // Check authorization
        compareJwtEmailIdAndCustomerEmailId(request, jwtService, emailId);

        String result = categoryService.delete(categoryId);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.CREATED.value(), "Category is deleted successfully", result);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Update a category by categoryId.
     *
     * @param categoryId  The categoryId to update.
     * @param request     The HTTP request.
     * @param categoryDto The updated category DTO.
     * @return ResponseEntity with ApiResponse indicating success or failure.
     * @throws UnAuthorizedUserException    if the user is not authorized.
     * @throws InvalidCategoryTypeException if the category type is invalid.
     */
    @PostMapping("/{categoryId}/updateCategory")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable int categoryId,
                                                      HttpServletRequest request,
                                                      @RequestBody @Valid CategoryDto categoryDto
    ) throws UnAuthorizedUserException, InvalidCategoryTypeException {
        log.info("inside updateProduct method controller");
        // Get email from JWT(request)
        String emailId = getEmailFromJwt(request);

        // Check if emailId belongs to an admin
        adminAuthorized(emailId);

        // Check authorization
        compareJwtEmailIdAndCustomerEmailId(request, jwtService, emailId);

        String result = categoryService.update(categoryId, emailId, categoryDto);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.CREATED.value(), "Category is updated successfully", result);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Get a list of all categories.
     *
     * @return ResponseEntity with ApiResponse containing a list of categories.
     */
    @GetMapping(value = "/getAllCategory")
    public ResponseEntity<ApiResponse> getAllCategory() {
        log.info("display all category ");
        List<Category> list = categoryService.dispAllCategory();
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "List of category", list);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
