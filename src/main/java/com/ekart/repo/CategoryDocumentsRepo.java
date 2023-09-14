package com.ekart.repo;

import com.ekart.model.Category;
import com.ekart.model.CategoryDocuments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryDocumentsRepo extends JpaRepository<CategoryDocuments,Integer> {
    List<CategoryDocuments> findByCategoryCategoryIdAndIsActive(Integer categoryId,boolean status);
}
