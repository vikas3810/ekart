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

import java.util.Set;

import static com.ekart.util.AuthenticationHelper.compareJwtEmailIdAndCustomerEmailId;
import static com.ekart.util.AuthenticationHelper.getEmailFromJwt;

@RestController
@RequestMapping("/address")
@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
public class AddressController {
    private final JwtService jwtService;
    private final AddressService addressService;

    /**
     * Add a new address for the user.
     *
     * @param addressDto The address information to add.
     * @param request    The HTTP request.
     * @return ResponseEntity containing ApiResponse with the added Address.
     * @throws UnAuthorizedUserException if the user is not authorized.
     */
    @PostMapping("/addAddress")
    public ResponseEntity<ApiResponse> addAddress(@RequestBody @Valid AddressDto addressDto,
                                                  HttpServletRequest request
    ) throws UnAuthorizedUserException {
        log.info("addAddress called");

        // Get email from JWT(request)
        String emailId = getEmailFromJwt(request);

        // Check authorization
        compareJwtEmailIdAndCustomerEmailId(request, jwtService, emailId);

        // Add the address
        Address result = addressService.addAddress(addressDto, emailId);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.CREATED.value(), "Address is added successfully", result);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Delete an address for the user.
     *
     * @param addressId The ID of the address to delete.
     * @param request   The HTTP request.
     * @return ResponseEntity containing ApiResponse with the deletion result message.
     * @throws UnAuthorizedUserException if the user is not authorized.
     */
    @PostMapping("/{addressId}/deleteAddress")
    public ResponseEntity<ApiResponse> deleteAddress(@PathVariable int addressId,
                                                     HttpServletRequest request
    ) throws UnAuthorizedUserException {
        log.info("inside deleteAddress method controller");

        // Get email from JWT(request)
        String emailId = getEmailFromJwt(request);

        // Check authorization
        compareJwtEmailIdAndCustomerEmailId(request, jwtService, emailId);

        // Delete the address
        String result = addressService.deleteAddress(addressId);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.CREATED.value(), "Address is deleted successfully", result);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Update an existing address for the user.
     *
     * @param addressId  The ID of the address to update.
     * @param request    The HTTP request.
     * @param addressDto The updated address information.
     * @return ResponseEntity containing ApiResponse with the updated Address.
     * @throws UnAuthorizedUserException if the user is not authorized.
     */
    @PostMapping("/{addressId}/updateAddress")
    public ResponseEntity<ApiResponse> updateAddress(@PathVariable int addressId,
                                                     HttpServletRequest request,
                                                     @RequestBody @Valid AddressDto addressDto
    ) throws UnAuthorizedUserException {
        log.info("inside updateAddress method controller");

        // Get email from JWT(request)
        String emailId = getEmailFromJwt(request);

        // Check authorization
        compareJwtEmailIdAndCustomerEmailId(request, jwtService, emailId);

        // Update the address
        Address result = addressService.updateAddress(addressId, addressDto);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.CREATED.value(), "Address is updated successfully", result);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Get all addresses for the user.
     *
     * @param request The HTTP request.
     * @return ResponseEntity containing ApiResponse with a list of addresses.
     * @throws UnAuthorizedUserException if the user is not authorized.
     */
    @GetMapping(value = "/getAllAddress")
    public ResponseEntity<ApiResponse> getAllAddress(HttpServletRequest request) throws UnAuthorizedUserException {
        log.info("display all addresses");

        // Get email from JWT(request)
        String emailId = getEmailFromJwt(request);

        // Check authorization
        compareJwtEmailIdAndCustomerEmailId(request, jwtService, emailId);

        // Get all addresses for the user
        Set<Address> addressList = addressService.getAllAddress(emailId);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "List of addresses", addressList);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
