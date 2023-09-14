package com.ekart.repo;

import com.ekart.model.CategoryDocuments;
import com.ekart.model.ProductDocuments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDocumentsRepo extends JpaRepository<ProductDocuments,Integer> {
    List<ProductDocuments> findByProductProductIdAndIsActive(int productId,boolean status);
}
