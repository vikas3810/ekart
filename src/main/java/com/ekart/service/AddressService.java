package com.ekart.service;

import com.ekart.dto.request.AddressDto;
import com.ekart.model.Address;
import com.ekart.model.Category;
import jakarta.validation.Valid;

import java.util.List;

public interface AddressService {
    String addAddress(@Valid AddressDto addressDto,String emailId);
    String deleteAddress(@Valid int addressId);
    

    String updateAddress(int addressId, AddressDto addressDto);

    List<Address> getAllAddress(String emailId);
}
