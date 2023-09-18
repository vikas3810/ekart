package com.ekart.controller;

import com.ekart.dto.request.CartDto;
import com.ekart.dto.response.ApiResponse;
import com.ekart.exception.DocumentAlreadyExistsException;
import com.ekart.exception.EntityAlreadyExistException;
import com.ekart.exception.InvalidCategoryTypeException;
import com.ekart.exception.UnAuthorizedUserException;
import com.ekart.jwt.JwtService;
import com.ekart.model.Cart;
import com.ekart.model.CartProduct;
import com.ekart.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ekart.util.AuthenticationHelper.*;

@RestController
@RequestMapping("/cart")
@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final JwtService jwtService;

    // Add a product to the cart
    @PostMapping("/addToCart")
    public ResponseEntity<ApiResponse> addToCart(@RequestBody @Valid CartDto cartDto,
                                                 HttpServletRequest request
    ) throws UnAuthorizedUserException, EntityAlreadyExistException, InvalidCategoryTypeException, DocumentAlreadyExistsException {
        log.info("addToCart called in cart controller ");
        // Get email from JWT(request)
        String emailId = getEmailFromJwt(request);

        // Check authorization
        compareJwtEmailIdAndCustomerEmailId(request, jwtService, emailId);

        int result = cartService.addToCart(cartDto, emailId);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.CREATED.value(), "Cart is created successfully", result);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    // Retrieve the user's cart
    @GetMapping("/getCart")
    public ResponseEntity<ApiResponse> getCart(HttpServletRequest request
    ) throws UnAuthorizedUserException, EntityAlreadyExistException, InvalidCategoryTypeException, DocumentAlreadyExistsException {
        log.info("getCart called in cart controller ");
        // Get email from JWT(request)
        String emailId = getEmailFromJwt(request);

        // Check authorization
        compareJwtEmailIdAndCustomerEmailId(request, jwtService, emailId);

        Cart result = cartService.getCart(emailId);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.CREATED.value(), "Cart retrieve successfully", result);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    // Delete a product from the cart by productId
    @GetMapping("/deleteFromCartProductByProductId/{productId}")
    public ResponseEntity<ApiResponse> deleteFromCartProductByProductId(
            @PathVariable @Valid int productId, HttpServletRequest request
    ) throws UnAuthorizedUserException, EntityAlreadyExistException, InvalidCategoryTypeException, DocumentAlreadyExistsException {
        log.info("deleteFromCartProductByProductId called in cart controller ");
        // Get email from JWT(request)
        String emailId = getEmailFromJwt(request);

        // Check authorization
        compareJwtEmailIdAndCustomerEmailId(request, jwtService, emailId);

        String result = cartService.deleteFromCart(productId, emailId);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.CREATED.value(), "Product deleted successfully from cart", result);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


    @GetMapping("/updateCart/{productId}/{quantity}")
    public ResponseEntity<ApiResponse> updateCart(
            @PathVariable @Valid int productId,
            @PathVariable @Valid int quantity,HttpServletRequest request
    ) throws UnAuthorizedUserException, EntityAlreadyExistException, InvalidCategoryTypeException, DocumentAlreadyExistsException {
        log.info("updateCart called in cart controller ");
        // Get email from JWT(request)
        String emailId = getEmailFromJwt(request);

        // Check authorization
        compareJwtEmailIdAndCustomerEmailId(request, jwtService, emailId);

        CartProduct result = cartService.update(productId, emailId,quantity);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.CREATED.value(), "Cart updated successfully", result);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
