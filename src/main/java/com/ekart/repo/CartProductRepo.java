package com.ekart.repo;

import com.ekart.model.CartProduct;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartProductRepo extends JpaRepository<CartProduct,Integer> {

    Optional<CartProduct> findByCartCartIdAndProductProductId(int cartId, int productId);
    @Transactional
    @Modifying
    @Query("UPDATE CartProduct cp SET cp.quantity = :quantity WHERE cp.cart.cartId = :cartId AND cp.product.productId = :productId")
    void updateQuantityByCartIdAndProductId(@Param("quantity") int quantity,@Param("cartId") int cartId, @Param("productId") int productId);

}
