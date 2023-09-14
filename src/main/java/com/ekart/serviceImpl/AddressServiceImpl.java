package com.ekart.serviceImpl;

import com.ekart.dto.request.AddressDto;
import com.ekart.model.Address;
import com.ekart.model.User;
import com.ekart.repo.AddressRepo;
import com.ekart.repo.UserRepo;
import com.ekart.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepo addressRepo;
    private final UserRepo userRepo;
    @Override
    public String addAddress(@Valid AddressDto addressDto,String emailId) {
        User user = userRepo.findByEmailId(emailId).orElseThrow(ResourceNotFoundException::new);
        Address address = Address.builder()
                .addressLine1(addressDto.getAddressLine1())
                .addressLine2(addressDto.getAddressLine2())
                .city(addressDto.getCity())
                .pinCode(addressDto.getPinCode())
                .state(addressDto.getState())
                .user(user)
                .build();
        addressRepo.save(address);
        return "Address Added";
    }

    @Override
    public String deleteAddress( int addressId) {
        Address address = addressRepo.findById(addressId).orElseThrow(ResourceNotFoundException::new);
        addressRepo.delete(address);
        return "Address deleted";
    }

    @Override
    public String updateAddress(int addressId, AddressDto addressDto) {
        Address address = addressRepo.findById(addressId).orElseThrow(ResourceNotFoundException::new);
         address.setAddressLine1(addressDto.getAddressLine1());
        address.setAddressLine2(addressDto.getAddressLine2());
        address.setCity(addressDto.getCity());
        address.setPinCode(addressDto.getPinCode());
        address.setState(addressDto.getState());

        addressRepo.save(address);
        return "Address updated";
    }

    @Override
    public List<Address> getAllAddress(String emailId) {
        User user = userRepo.findByEmailId(emailId).orElseThrow(ResourceNotFoundException::new);
        return addressRepo.findByUserUserId(user.getUserId());
    }
}
