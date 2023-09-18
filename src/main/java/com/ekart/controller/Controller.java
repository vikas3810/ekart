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
    @GetMapping(value = "/getAllCategory")

    public ResponseEntity<ApiResponse> getAllCategory() {
        log.info("display all category ");
        List<Category> list = categoryService.dispAllCategory();
        List<Category> p = list.stream().filter(Category::isActive).collect(Collectors.toList());
        ApiResponse apiresponse = new ApiResponse(HttpStatus.OK.value(), "List of category", list);
        return new ResponseEntity<>(apiresponse, HttpStatus.OK);
    }

    @GetMapping(value = "/{categoryId}/getCategoryImagesByCategoryId")

    public ResponseEntity<ApiResponse> getCategoryImagesByCategoryId(@PathVariable int categoryId) {
        log.info("display all category image");
        List<CategoryDocuments> list = categoryDocumentsService.getCategoryImagesByCategoryId(categoryId);
        ApiResponse apiresponse = new ApiResponse(HttpStatus.OK.value(), "List of images", list);
        return new ResponseEntity<>(apiresponse, HttpStatus.OK);
    }
    @GetMapping(value = "/{categoryId}/getCategoryByCategoryId")

    public ResponseEntity<ApiResponse> getCategoryByCategoryId(@PathVariable int categoryId) {
        log.info("display all category image");
        Category category = categoryRepo.findById(categoryId).orElseThrow(ResourceNotFoundException::new);
        ApiResponse apiresponse = new ApiResponse(HttpStatus.OK.value(), "Category Details", category);
        return new ResponseEntity<>(apiresponse, HttpStatus.OK);
    }
    @GetMapping(value = "/getAllProduct")

    public ResponseEntity<ApiResponse> getAllProduct() {
        log.info("get All Product ");
        List<Product> list = productService.dispAllProduct();
        ApiResponse apiresponse = new ApiResponse(HttpStatus.OK.value(), "List of product", list);
        return new ResponseEntity<>(apiresponse, HttpStatus.OK);
    }




    @GetMapping(value = "/{productId}/getProductImagesByProductId")

    public ResponseEntity<ApiResponse> getProductImagesByProductId(@PathVariable int productId) {
        log.info("display all product image");
        List<ProductDocuments> list = productDocumentsService.getProductImagesByProductId(productId);
        ApiResponse apiresponse = new ApiResponse(HttpStatus.OK.value(), "List of images", list);
        return new ResponseEntity<>(apiresponse, HttpStatus.OK);
    }


    @GetMapping(value = "/{productId}/getProductByProductId")

    public ResponseEntity<ApiResponse> getProductByProductId(@PathVariable int productId) {
        log.info("display all product image");
        Product product = productRepo.findById(productId).orElseThrow(ResourceNotFoundException::new);

        ApiResponse apiresponse = new ApiResponse(HttpStatus.OK.value(), "Product Details", product);
        return new ResponseEntity<>(apiresponse, HttpStatus.OK);
    }
}
