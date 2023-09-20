package com.ekart.controller;

import com.ekart.dto.request.CartDto;
import com.ekart.dto.response.ApiResponse;
import com.ekart.exception.UnAuthorizedUserException;
import com.ekart.jwt.JwtService;
import com.ekart.model.Address;
import com.ekart.model.Orders;
import com.ekart.service.CartService;
import com.ekart.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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

    @GetMapping("/orderNow/{addressId}")
    public ResponseEntity<ApiResponse> orderNow(HttpServletRequest request,
                                                @PathVariable int addressId
    ) throws UnAuthorizedUserException {
        log.info("orderNow called in controller ");
        // Get email from JWT(request)
        String emailId = getEmailFromJwt(request);

        // Check authorization
        compareJwtEmailIdAndCustomerEmailId(request, jwtService, emailId);

        String result = orderService.orderNow(emailId,addressId);
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




    @GetMapping(value = "/getAllOrders")

    public ResponseEntity<ApiResponse> getAllOrders(HttpServletRequest request) throws UnAuthorizedUserException {
        log.info("display all orders ");
        //get email from JWT(request)
        String emailId = getEmailFromJwt(request);

        //check authorization
        compareJwtEmailIdAndCustomerEmailId(request,jwtService,emailId);
        List<Orders> list = orderService.getAllOrder(emailId);
        ApiResponse apiresponse = new ApiResponse(HttpStatus.OK.value(), "List of orders", list);
        return new ResponseEntity<>(apiresponse, HttpStatus.OK);
    }
}
