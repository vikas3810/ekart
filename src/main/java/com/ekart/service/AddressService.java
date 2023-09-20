package com.ekart.service;

import com.ekart.dto.request.AddressDto;
import com.ekart.model.Address;
import com.ekart.model.Category;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Set;

public interface AddressService {
    Address addAddress(@Valid AddressDto addressDto,String emailId);
    String deleteAddress(@Valid int addressId);
    

    Address updateAddress(int addressId, AddressDto addressDto);

    Set<Address> getAllAddress(String emailId);
}
