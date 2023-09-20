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

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepo addressRepo;
    private final UserRepo userRepo;

    /**
     * Adds a new address for a user.
     *
     * @param addressDto The DTO containing address information.
     * @param emailId    The email ID of the user.
     * @return A message indicating the address has been added.
     */
    @Override
    public Address addAddress(@Valid AddressDto addressDto, String emailId) {
        User user = userRepo.findByEmailId(emailId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));
        Address address = Address.builder()
                .addressLine1(addressDto.getAddressLine1())
                .addressLine2(addressDto.getAddressLine2())
                .city(addressDto.getCity())
                .pinCode(addressDto.getPinCode())
                .state(addressDto.getState())
                .user(user)
                .build();
       return addressRepo.save(address);
    }

    /**
     * Deletes an existing address by its ID.
     *
     * @param addressId The ID of the address to be deleted.
     * @return A message indicating the address has been deleted.
     */
    @Override
    public String deleteAddress(int addressId) {
        Address address = addressRepo.findById(addressId)
                .orElseThrow(()-> new ResourceNotFoundException("Address not found"));
        addressRepo.delete(address);
        return "Address deleted";
    }

    /**
     * Updates an existing address by its ID.
     *
     * @param addressId  The ID of the address to be updated.
     * @param addressDto The DTO containing updated address information.
     * @return A message indicating the address has been updated.
     */
    @Override
    public Address updateAddress(int addressId, AddressDto addressDto) {
        Address address = addressRepo.findById(addressId)
                .orElseThrow(()-> new ResourceNotFoundException("Address not found"));
        address.setAddressLine1(addressDto.getAddressLine1());
        address.setAddressLine2(addressDto.getAddressLine2());
        address.setCity(addressDto.getCity());
        address.setPinCode(addressDto.getPinCode());
        address.setState(addressDto.getState());
        return addressRepo.save(address);
    }

    /**
     * Retrieves all addresses associated with a user by their email ID.
     *
     * @param emailId The email ID of the user.
     * @return A list of addresses belonging to the user.
     */
    @Override
    public Set<Address> getAllAddress(String emailId) {
        User user = userRepo.findByEmailId(emailId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));
        return addressRepo.findByUserUserId(user.getUserId());
    }
}
