package com.ekart.controller;

import com.ekart.dto.request.AddressDto;
import com.ekart.dto.response.ApiResponse;
import com.ekart.exception.UnAuthorizedUserException;
import com.ekart.jwt.JwtService;
import com.ekart.model.Address;
import com.ekart.service.AddressService;
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
@RequestMapping("/address")
@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
public class AddressController {
    private final JwtService jwtService;
    private final AddressService addressService;
    @PostMapping("/addAddress")

    public ResponseEntity<ApiResponse> addAddress(@RequestBody @Valid AddressDto addressDto,
                                                   HttpServletRequest request
    ) throws UnAuthorizedUserException {
        log.info("addAddress called  ");
        //get email from JWT(request)
        String emailId = getEmailFromJwt(request);

        //check authorization
        compareJwtEmailIdAndCustomerEmailId(request,jwtService,emailId);

        String authenticationResponse = addressService.addAddress(addressDto,emailId);
        ApiResponse apiresponse = new ApiResponse(HttpStatus.CREATED.value(), "Address is added successfully", authenticationResponse);
        return new ResponseEntity<>(apiresponse, HttpStatus.OK);
    }

    @PostMapping("/{addressId}/deleteAddress")

    public ResponseEntity<ApiResponse> deleteAddress(@PathVariable int addressId,
                                                      HttpServletRequest request
    ) throws UnAuthorizedUserException {
        log.info("inside deleteAddress method controller");
        //get email from JWT(request)
        String emailId = getEmailFromJwt(request);

        //check authorization
        compareJwtEmailIdAndCustomerEmailId(request,jwtService,emailId);

        String result = addressService.deleteAddress(addressId);
        ApiResponse apiresponse = new ApiResponse(HttpStatus.CREATED.value(), "Address is deleted successfully", result);
        return new ResponseEntity<>(apiresponse, HttpStatus.OK);
    }


    @PostMapping("/{addressId}/updateAddress")

    public ResponseEntity<ApiResponse> updateAddress(@PathVariable int addressId,
                                                      HttpServletRequest request,
                                                      @RequestBody @Valid AddressDto addressDto
    ) throws UnAuthorizedUserException {
        log.info("inside updateAddress method controller");
        //get email from JWT(request)
        String emailId = getEmailFromJwt(request);

        //check authorization
        compareJwtEmailIdAndCustomerEmailId(request,jwtService,emailId);

        String result = addressService.updateAddress(addressId,addressDto);
        ApiResponse apiresponse = new ApiResponse(HttpStatus.CREATED.value(), "Address is updated successfully", result);
        return new ResponseEntity<>(apiresponse, HttpStatus.OK);
    }

    @GetMapping(value = "/getAllAddress")

    public ResponseEntity<ApiResponse> getAllAddress(HttpServletRequest request) throws UnAuthorizedUserException {
        log.info("display all addresses ");
        //get email from JWT(request)
        String emailId = getEmailFromJwt(request);

        //check authorization
        compareJwtEmailIdAndCustomerEmailId(request,jwtService,emailId);
        List<Address> list = addressService.getAllAddress(emailId);
        ApiResponse apiresponse = new ApiResponse(HttpStatus.OK.value(), "List of address", list);
        return new ResponseEntity<>(apiresponse, HttpStatus.OK);
    }
}
