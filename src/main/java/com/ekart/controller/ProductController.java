package com.ekart.controller;

import com.ekart.dto.request.ProductDto;
import com.ekart.dto.response.ApiResponse;
import com.ekart.exception.EntityAlreadyExistException;
import com.ekart.exception.UnAuthorizedUserException;
import com.ekart.jwt.JwtService;
import com.ekart.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ekart.util.AuthenticationHelper.*;

@RestController
@RequestMapping("/product")
@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
public class ProductController {
    private final JwtService jwtService;
    private final ProductService productService;

    /**
     * Add a new product.
     *
     * @param productDto The product details to add.
     * @param request    The HTTP request.
     * @return ResponseEntity with ApiResponse indicating success or failure.
     * @throws EntityAlreadyExistException if the product already exists.
     * @throws UnAuthorizedUserException   if the user is not authorized.
     */
    @PostMapping("/addProduct")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody @Valid ProductDto productDto,
                                                  HttpServletRequest request
    ) throws EntityAlreadyExistException, UnAuthorizedUserException {
        log.info("inside addProduct method controller");
        // Get email from JWT(request)
        String emailId = getEmailFromJwt(request);

        // Check if emailId belongs to admin or not (contains @ekart)
        adminAuthorized(emailId);

        // Check authorization
        compareJwtEmailIdAndCustomerEmailId(request, jwtService, emailId);

        int authenticationResponse = productService.addProduct(productDto, emailId);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.CREATED.value(), "Product is created successfully", authenticationResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Delete an existing product by productId.
     *
     * @param productId The productId to delete.
     * @param request   The HTTP request.
     * @return ResponseEntity with ApiResponse indicating success or failure.
     * @throws UnAuthorizedUserException if the user is not authorized.
     */
    @PostMapping("/{productId}/deleteProduct")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable int productId,
                                                     HttpServletRequest request
    ) throws UnAuthorizedUserException {
        log.info("inside deleteProduct method controller");
        // Get email from JWT(request)
        String emailId = getEmailFromJwt(request);

        // Check if emailId belongs to admin or not (contains @ekart)
        adminAuthorized(emailId);

        // Check authorization
        compareJwtEmailIdAndCustomerEmailId(request, jwtService, emailId);

        String result = productService.delete(productId);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.CREATED.value(), "Product is deleted successfully", result);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Update an existing product by productId.
     *
     * @param productId  The productId to update.
     * @param request    The HTTP request.
     * @param productDto The product details to update.
     * @return ResponseEntity with ApiResponse indicating success or failure.
     * @throws UnAuthorizedUserException if the user is not authorized.
     */
    @PostMapping("/{productId}/updateProduct")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable int productId,
                                                     HttpServletRequest request,
                                                     @RequestBody @Valid ProductDto productDto
    ) throws UnAuthorizedUserException {
        log.info("inside updateProduct method controller");
        // Get email from JWT(request)
        String emailId = getEmailFromJwt(request);

        // Check if emailId belongs to admin or not (contains @ekart)
        adminAuthorized(emailId);

        // Check authorization
        compareJwtEmailIdAndCustomerEmailId(request, jwtService, emailId);

        String result = productService.update(productId, emailId, productDto);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.CREATED.value(), "Product is updated successfully", result);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
