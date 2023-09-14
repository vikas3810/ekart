package com.ekart.repo;

import com.ekart.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepo extends JpaRepository<Address,Integer> {
List<Address> findByUserUserId(int userId);
}
