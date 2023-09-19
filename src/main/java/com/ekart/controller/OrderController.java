package com.ekart.controller;

import com.ekart.dto.request.CartDto;
import com.ekart.dto.response.ApiResponse;
import com.ekart.exception.UnAuthorizedUserException;
import com.ekart.jwt.JwtService;
import com.ekart.service.CartService;
import com.ekart.service.OrderService;
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
@RequestMapping("/order")
@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final CartService cartService;
    private final JwtService jwtService;
    private final OrderService orderService;

    @GetMapping("/orderNow")
    public ResponseEntity<ApiResponse> orderNow(HttpServletRequest request
    ) throws UnAuthorizedUserException {
        log.info("orderNow called in controller ");
        // Get email from JWT(request)
        String emailId = getEmailFromJwt(request);

        // Check authorization
        compareJwtEmailIdAndCustomerEmailId(request, jwtService, emailId);

        String result = orderService.orderNow(emailId);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.CREATED.value(), "Order placed successfully", result);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


    @GetMapping("/cancelOrder/{orderId}")
    public ResponseEntity<ApiResponse> cancelOrder(@PathVariable @Valid int orderId,
            HttpServletRequest request
    ) throws UnAuthorizedUserException {
        log.info("cancelOrder called in controller ");
        // Get email from JWT(request)
        String emailId = getEmailFromJwt(request);

        // Check authorization
        compareJwtEmailIdAndCustomerEmailId(request, jwtService, emailId);

        String result = orderService.cancelOrder(emailId,orderId);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.CREATED.value(), "Order cancel successfully", result);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
