package com.ekart.repo;

import com.ekart.model.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartProductRepo extends JpaRepository<CartProduct,Integer> {

    Optional<CartProduct> findByCartCartIdAndProductProductId(int cartId, int productId);
}
