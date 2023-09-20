package com.ekart.repo;

import com.ekart.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface AddressRepo extends JpaRepository<Address,Integer> {
Set<Address> findByUserUserId(int userId);
}
