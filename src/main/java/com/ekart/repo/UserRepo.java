package com.ekart.repo;

import com.ekart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Integer> {

    Optional<User> findByEmailId(String emailId);
    boolean existsByEmailId(String emailId);

}
