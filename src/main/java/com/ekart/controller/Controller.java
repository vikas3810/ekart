package com.ekart.controller;

import com.ekart.dto.response.ApiResponse;
import com.ekart.model.Category;
import com.ekart.model.CategoryDocuments;
import com.ekart.model.Product;
import com.ekart.model.ProductDocuments;
import com.ekart.repo.CategoryRepo;
import com.ekart.repo.ProductRepo;
import com.ekart.service.CategoryDocumentsService;
import com.ekart.service.CategoryService;
import com.ekart.service.ProductDocumentsService;
import com.ekart.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/controller")
@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
public class Controller {
    private final CategoryService categoryService;
    private final CategoryDocumentsService categoryDocumentsService;
    private final ProductDocumentsService productDocumentsService;
    private final ProductService productService;
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;

    /**
     * Get all categories.
     *
     * @return ResponseEntity containing a list of categories.
     */
    @GetMapping(value = "/getAllCategory")
    public ResponseEntity<ApiResponse> getAllCategory() {
        log.info("Display all categories");
        List<Category> list = categoryService.dispAllCategory();
        List<Category> activeCategories = list.stream().filter(Category::isActive).collect(Collectors.toList());
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "List of categories", activeCategories);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Get category images by category ID.
     *
     * @param categoryId The category ID.
     * @return ResponseEntity containing a list of category images.
     */
    @GetMapping(value = "/{categoryId}/getCategoryImagesByCategoryId")
    public ResponseEntity<ApiResponse> getCategoryImagesByCategoryId(@PathVariable int categoryId) {
        log.info("Display all category images");
        List<CategoryDocuments> list = categoryDocumentsService.getCategoryImagesByCategoryId(categoryId);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "List of images", list);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Get category details by category ID.
     *
     * @param categoryId The category ID.
     * @return ResponseEntity containing category details.
     */
    @GetMapping(value = "/{categoryId}/getCategoryByCategoryId")
    public ResponseEntity<ApiResponse> getCategoryByCategoryId(@PathVariable int categoryId) {
        log.info("Display category details");
        Category category = categoryRepo.findById(categoryId).orElseThrow(ResourceNotFoundException::new);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "Category Details", category);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Get all products.
     *
     * @return ResponseEntity containing a list of products.
     */
    @GetMapping(value = "/getAllProduct")
    public ResponseEntity<ApiResponse> getAllProduct() {
        log.info("Get All Products");
        List<Product> list = productService.dispAllProduct();
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "List of products", list);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Get product images by product ID.
     *
     * @param productId The product ID.
     * @return ResponseEntity containing a list of product images.
     */
    @GetMapping(value = "/{productId}/getProductImagesByProductId")
    public ResponseEntity<ApiResponse> getProductImagesByProductId(@PathVariable int productId) {
        log.info("Display all product images");
        List<ProductDocuments> list = productDocumentsService.getProductImagesByProductId(productId);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "List of images", list);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Get product details by product ID.
     *
     * @param productId The product ID.
     * @return ResponseEntity containing product details.
     */
    @GetMapping(value = "/{productId}/getProductByProductId")
    public ResponseEntity<ApiResponse> getProductByProductId(@PathVariable int productId) {
        log.info("Display product details");
        Product product = productRepo.findById(productId).orElseThrow(ResourceNotFoundException::new);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "Product Details", product);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
