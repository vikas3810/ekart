package com.ekart.repo;

import com.ekart.model.Category;
import com.ekart.model.CategoryType;
import com.ekart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category,Integer> {
    List<Category> findAllByIsActive(boolean status);
    Optional<Category> findByCategoryType(CategoryType categoryType);
}
