package com.ekart.serviceImpl;

import com.ekart.dto.request.CartDto;
import com.ekart.model.Cart;
import com.ekart.model.CartProduct;
import com.ekart.model.Product;
import com.ekart.model.User;
import com.ekart.repo.CartProductRepo;
import com.ekart.repo.CartRepo;
import com.ekart.repo.ProductRepo;
import com.ekart.repo.UserRepo;
import com.ekart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Component
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepo cartRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private final CartProductRepo cartProductRepo;

    @Override
    public int addToCart(CartDto cartDto, String emailId) {
        log.info("Inside addToCart method");

        // Retrieve the user entity
        User user = userRepo.findByEmailId(emailId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Retrieve the product entity
        Product product = productRepo.findById(cartDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));


        // Try to find an existing cart for the user
        Cart cart = cartRepo.findByUserUserId(user.getUserId())
                .orElseGet(() -> createNewCartForUser(user));

        log.info("Cart available for user: " + cart.getCartId());

        // Check if the product is already in the cart
        Optional<CartProduct> existingProduct = cart.getCartProducts().stream()
                .filter(p -> p.getCartProductId() == cartDto.getProductId())
                .findFirst();

        if (existingProduct.isPresent()) {
            log.info("Product available in cart");
            // Update the quantity and subtotal
            double newSubTotal = calculateNewSubTotal(cart.getSubTotal(), cartDto.getQuantity(), product.getPrice());
            cart.setSubTotal(newSubTotal);
        } else {
            CartProduct cartProduct = CartProduct.builder().build();
            // Add the product to the cart
            cart.getCartProducts().add(cartProduct);
            cart.setSubTotal(cart.getSubTotal() + calculateSubTotal(cartDto.getQuantity(), product.getProductId()));
        }

        // Save the updated cart
        cart.setActive(true);
        Cart savedCart = cartRepo.save(cart);

        // Update or create cart-product relationship
        updateCartProductRelationship(savedCart, product, cartDto.getQuantity());

        return savedCart.getCartId();
    }

    //Helper method
    private Cart createNewCartForUser(User user) {
        log.info("Cart not available for user, creating a new cart");
        return Cart.builder()
                .user(user)
                .isActive(true)
                .subTotal(0.0)
                .cartProducts(new ArrayList<>())
                .build();
    }
    private double calculateSubTotal(int quantity, int productId) {
        return quantity * productId;
    }

    private double calculateNewSubTotal(double oldSubTotal, int quantity, double price) {
        return oldSubTotal+(quantity*price);
    }
    //Helper method update CartProductRelationship
    private void updateCartProductRelationship(Cart cart, Product product, int quantity) {
        log.info("updateCartProductRelationship ");
        Optional<CartProduct> cartProduct = cartProductRepo
                .findByCartCartIdAndProductProductId(cart.getCartId(), product.getProductId());

        if (cartProduct.isPresent()) {
            // Update the existing relationship
            CartProduct p = cartProduct.get();
            p.setQuantity(p.getQuantity() + quantity);
        } else {
            // Create a new cart-product relationship
            CartProduct newCartProduct = CartProduct.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();
            cartProductRepo.save(newCartProduct);
        }
    }


    @Override
    public String deleteFromCart(int productId, String emailId) {
        log.info("inside delete product From Cart method");
        // Find the user by email ID
        User user = userRepo.findByEmailId(emailId).orElseThrow(ResourceNotFoundException::new);
        // Find the user's cart
        Cart cart = cartRepo.findByUserUserId(user.getUserId()).orElseThrow(ResourceNotFoundException::new);
        // Get the list of products in the cart
        List<Product> productList = cart.getProduct();
        // Find the product to be deleted by its ID
        Optional<Product> productToDelete = productList.stream()
                .filter(product -> product.getProductId() == productId)
                .findFirst();
        if (productToDelete.isPresent()) {
            // Remove the product from the cart
            productList.remove(productToDelete.get());
            cart.setSubTotal(cart.getSubTotal()-productToDelete.get().getPrice());
            // Update the cart in the database
            cartRepo.save(cart);
            return "Product deleted from the cart.";
        } else {
            return "Product not found in the cart.";
        }
    }


    @Override
    public CartProduct update(int productId, String emailId, int quantity) {
        log.info("update cart");
        // Find the user by email ID
        User user = userRepo.findByEmailId(emailId).orElseThrow(ResourceNotFoundException::new);
        // Find the user's cart
        Cart cart = cartRepo.findByUserUserId(user.getUserId()).orElseThrow(ResourceNotFoundException::new);

       Optional<CartProduct> cart1 = cartProductRepo.findByCartCartIdAndProductProductId(cart.getCartId(),productId);
        // Get the list of products in the cart
        CartProduct cart2 = new CartProduct();
        if (cart1.isPresent()){
             cart2=cart1.get();
        cart2.setQuantity(cart2.getQuantity()+quantity);
            System.out.println("................"+cart2);
        }

        List<Product> productList = cart.getProduct();
        return cart2;
    }

    @Override
    public Cart getCart(String emailId) {
        log.info("get user cart by emailId");
        User user = userRepo.findByEmailId(emailId).orElseThrow(ResourceNotFoundException::new);
        return cartRepo.findByUserUserId(user.getUserId()).orElseThrow(ResourceNotFoundException::new);
    }
}
