package com.ekart.controller;

import com.ekart.dto.request.CartDto;
import com.ekart.dto.response.ApiResponse;
import com.ekart.exception.UnAuthorizedUserException;
import com.ekart.jwt.JwtService;
import com.ekart.model.Cart;
import com.ekart.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ekart.util.AuthenticationHelper.compareJwtEmailIdAndCustomerEmailId;
import static com.ekart.util.AuthenticationHelper.getEmailFromJwt;

@RestController
@RequestMapping("/cart")
@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final JwtService jwtService;

    // Add a product to the cart
    @PostMapping("/addProductToCart")
    public ResponseEntity<ApiResponse> addProductToCart(
            @RequestBody @Valid CartDto cartDto,
            HttpServletRequest request
    ) throws UnAuthorizedUserException {
        log.info("addProductToCart called in cart controller");

        // Get email from JWT(request)
        String emailId = getEmailFromJwt(request);

        // Check authorization
        compareJwtEmailIdAndCustomerEmailId(request, jwtService, emailId);

        // Call the service to add the product to the cart
        int result = cartService.addProductToCart(cartDto, emailId);

        ApiResponse apiResponse = new ApiResponse(HttpStatus.CREATED.value(), "Cart is created successfully", result);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    // Retrieve the user's cart
    @GetMapping("/getCart")
    public ResponseEntity<ApiResponse> getCart(HttpServletRequest request) throws UnAuthorizedUserException {
        log.info("getCart called in cart controller");

        // Get email from JWT(request)
        String emailId = getEmailFromJwt(request);

        // Check authorization
        compareJwtEmailIdAndCustomerEmailId(request, jwtService, emailId);

        // Call the service to retrieve the user's cart
        Cart result = cartService.getCart(emailId);

        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "Cart retrieved successfully", result);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    // Delete a product from the cart by productId
    @GetMapping("/deleteFromCartProductByProductId/{productId}")
    public ResponseEntity<ApiResponse> deleteFromCartProductByProductId(
            @PathVariable @Valid int productId,
            HttpServletRequest request
    ) throws UnAuthorizedUserException {
        log.info("deleteFromCartProductByProductId called in cart controller");

        // Get email from JWT(request)
        String emailId = getEmailFromJwt(request);

        // Check authorization
        compareJwtEmailIdAndCustomerEmailId(request, jwtService, emailId);

        // Call the service to delete the product from the cart
        String result = cartService.deleteFromCart(productId, emailId);

        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "Product deleted successfully from cart", result);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    // Update the cart with a specific quantity for a product
    @PostMapping("/updateCart/{productId}/{quantity}")
    public ResponseEntity<ApiResponse> updateCart(
            @PathVariable @Valid int productId,
            @PathVariable @Valid int quantity,
            HttpServletRequest request
    ) throws UnAuthorizedUserException {
        log.info("updateCart called in cart controller");

        // Get email from JWT(request)
        String emailId = getEmailFromJwt(request);

        // Check authorization
        compareJwtEmailIdAndCustomerEmailId(request, jwtService, emailId);

        // Call the service to update the cart
        String result = cartService.update(productId, emailId, quantity);

        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "Cart updated successfully", result);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
