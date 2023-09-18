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

import java.time.LocalDateTime;
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
    public int addProductToCart(CartDto cartDto, String emailId) {
        log.info("Inside addProductToCart method");

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
        Optional<Product> existingProduct = cart.getProducts().stream()
                .filter(p -> p.getProductId() == cartDto.getProductId())
                .findFirst();

        if (existingProduct.isPresent()) {
            log.info("Product available in cart");
            // Update the quantity and subtotal
            double newSubTotal = calculateNewSubTotal(cart.getSubTotal(), cartDto.getQuantity(), product.getPrice());
            cart.setSubTotal(newSubTotal);
        } else {

            // Add the product to the cart
            cart.getProducts().add(product);
            cart.setSubTotal(cart.getSubTotal() + calculateSubTotal(cartDto.getQuantity(), product.getPrice()));
        }

        // Save the updated cart
        cart.setActive(true);
        Cart savedCart = cartRepo.save(cart);

        // Update or create cart-product relationship
        updateCartProductRelationship(savedCart, product, cartDto.getQuantity());

        return savedCart.getCartId();
    }
    private Cart createNewCartForUser(User user) {
        log.info("Cart not available for user, creating a new cart");
        return Cart.builder()
                .user(user)
                .isActive(true)
                .subTotal(0.0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .products(new ArrayList<>())
                .build();
    }
    private double calculateSubTotal(int quantity, double price) {
        return quantity * price;
    }
    private double calculateNewSubTotal(double oldSubTotal, int quantity, double price) {
        return oldSubTotal+(quantity*price);
    }

    private void updateCartProductRelationship(Cart cart, Product product, int quantity) {
        log.info("updateCartProductRelationship ");

        Optional<CartProduct> optionalCartProduct = cartProductRepo
                .findByCartCartIdAndProductProductId(cart.getCartId(), product.getProductId());

        if (optionalCartProduct.isPresent()) {
            log.info("update in cartProduct.......");
            // Update the existing relationship
            CartProduct cartProduct = optionalCartProduct.get();
            cartProduct.setSubtotal(calculateNewSubTotal(cartProduct.getSubtotal(),quantity,product.getPrice()));
            cartProduct.setQuantity(cartProduct.getQuantity() + quantity);
            cartProductRepo.save(cartProduct);
        } else {
            // Create a new cart-product relationship
            CartProduct newCartProduct = CartProduct.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .subtotal(quantity*product.getPrice())
                    .build();
            cartProductRepo.save(newCartProduct);
        }

    }
    @Override
    public String deleteFromCart(int productId, String emailId) {
        log.info("inside delete product From Cart method");

        // Retrieve the user entity
        User user = userRepo.findByEmailId(emailId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Try to find an existing cart for the user
        Cart cart = cartRepo.findByUserUserId(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        CartProduct cartProduct = cartProductRepo.findByCartCartIdAndProductProductId(cart.getCartId(),productId).orElseThrow(() -> new ResourceNotFoundException("CartProduct not found"));
             // Get the list of products in the cart
        List<Product> productList = cart.getProducts();

        // Find the product to be deleted by its ID
        Optional<Product> productToDeleteFromCart = productList.stream()
                .filter(product -> product.getProductId() == productId)
                .findFirst();


        if (productToDeleteFromCart.isPresent()) {
            // Remove the product from the cart
            productList.remove(productToDeleteFromCart.get());
            cart.setSubTotal(cart.getSubTotal()-(cartProduct.getQuantity()*productToDeleteFromCart.get().getPrice()));
           if(productList.isEmpty()){
               cart.setSubTotal(0.0);
           }
           cartProductRepo.delete(cartProduct);
            // Update the cart in the database
            cartRepo.save(cart);
            return "Product deleted from the cart.";
        } else {
            return "Product not found in the cart.";
        }
    }
    @Override
    public String update(int productId, String emailId, int quantity) {
        log.info("update cart");
        // Retrieve the user entity
        User user = userRepo.findByEmailId(emailId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Try to find an existing cart for the user
        Cart cart = cartRepo.findByUserUserId(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        CartProduct cartProduct = cartProductRepo.findByCartCartIdAndProductProductId(cart.getCartId(),productId).orElseThrow(() -> new ResourceNotFoundException("CartProduct not found"));
        // Get the list of products in the cart
        List<Product> productList = cart.getProducts();
        // Find the product to be deleted by its ID
        Optional<Product> productToDeleteFromCart = productList.stream()
                .filter(product -> product.getProductId() == productId)
                .findFirst();
        if (productToDeleteFromCart.isPresent()) {
            cart.setSubTotal(cart.getSubTotal() - ((cartProduct.getQuantity()-quantity) * productToDeleteFromCart.get().getPrice()));
            cartProduct.setQuantity(quantity);
            cartProduct.setSubtotal(quantity*productToDeleteFromCart.get().getPrice());

            if (productList.isEmpty()) {
                cart.setSubTotal(0.0);
            }

        }
       // Update the cart in the database
        cartProductRepo.save(cartProduct);
            cartRepo.save(cart);
        return "cart updated";
    }
    @Override
    public Cart getCart(String emailId) {
        log.info("get user cart by emailId");
        User user = userRepo.findByEmailId(emailId).orElseThrow(ResourceNotFoundException::new);
        return cartRepo.findByUserUserId(user.getUserId()).orElseThrow(ResourceNotFoundException::new);
    }
}
