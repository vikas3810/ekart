package com.ekart.serviceImpl;

import com.ekart.model.Cart;
import com.ekart.model.CartProduct;
import com.ekart.model.Orders;
import com.ekart.model.User;
import com.ekart.repo.CartProductRepo;
import com.ekart.repo.CartRepo;
import com.ekart.repo.OrderRepo;
import com.ekart.repo.UserRepo;
import com.ekart.service.CartService;
import com.ekart.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.ekart.util.JavaMail.sendOrderCancelMail;
import static com.ekart.util.JavaMail.sendOrderPlacedMail;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserRepo userRepo;
    private final OrderRepo orderRepo;
    private final CartRepo cartRepo;
    private final CartProductRepo cartProductRepo;
    private final CartService cartService;
    private final JavaMailSender javaMailSender;

    /**
     * Place an order for a user based on their cart contents.
     *
     * @param emailId The email ID of the user placing the order.
     * @return A message containing the order ID.
     * @throws ResourceNotFoundException if the user or cart is not found.
     */

    @Override
    public String orderNow(String emailId) {
        log.info("Inside orderNow method");

        // Retrieve the user entity
        User user = userRepo.findByEmailId(emailId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Find the cart for the user
        Cart cart = cartRepo.findByUserUserId(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        // Retrieve cart products that are not marked as deleted
        List<CartProduct> cartProductList = cartProductRepo.findByCartCartIdAndIsDeleted(cart.getCartId(), false);

        // Create an order
        Orders order = Orders.builder()
                .createdDate(LocalDateTime.now())
                .isActive(true)
                .modifiedDate(LocalDateTime.now())
                .product(cartProductList)
                .user(user)
                .build();

        // Perform payment processing
        boolean paymentSuccessful = processPayment(order);

        if (paymentSuccessful) {
            // Save the order to mark it as paid
            Orders saveOrder = orderRepo.save(order);
            int orderId = saveOrder.getOrderId();

            // Remove purchased products from the cart
            cartProductList.forEach(cartProduct ->
                    cartService.deleteFromCart(cartProduct.getProduct().getProductId(), emailId));

            // Send an order placed email
            String subject = "Order Placed Successfully";
            sendOrderPlacedMail(emailId, orderId, subject, javaMailSender);

            return "Order id : " + orderId;
        } else {

            return "Payment failed. Please try again.";
        }
    }

    private boolean processPayment(Orders order) {
//        OrderServiceImpl paymentGateway = null;
//        if (paymentGateway.processPayment(order)) {
//             return true;
//         } else {
//             return false;
//         }
        return true;
    }

//    public String orderNow(String emailId) {
//        log.info("Inside orderNow method");
//
//        // Retrieve the user entity
//        User user = userRepo.findByEmailId(emailId)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//        // Find the cart for the user
//        Cart cart = cartRepo.findByUserUserId(user.getUserId())
//                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
//
//        // Retrieve cart products that are not marked as deleted
//        List<CartProduct> cartProductList = cartProductRepo.findByCartCartIdAndIsDeleted(cart.getCartId(), false);
//
//        // Create an order
//        Orders order = Orders.builder()
//                .createdDate(LocalDateTime.now())
//                .isActive(true)
//                .modifiedDate(LocalDateTime.now())
//                .product(cartProductList)
//                .user(user)
//                .build();
//        Orders saveOrder = orderRepo.save(order);
//        int orderId = saveOrder.getOrderId();
//
//        // Remove purchased products from the cart
//        cartProductList.forEach(cartProduct ->
//                cartService.deleteFromCart(cartProduct.getProduct().getProductId(), emailId));
//
//        // Send an order placed email
//        String subject = "Order Placed Successfully";
//        sendOrderPlacedMail(emailId, orderId, subject, javaMailSender);
//
//        return "Order id : " + orderId;
//    }

    /**
     * Cancel an existing order for a user.
     *
     * @param emailId The email ID of the user canceling the order.
     * @param orderId The ID of the order to cancel.
     * @return A message indicating the order cancellation.
     * @throws ResourceNotFoundException if the user or order is not found.
     */
    @Override
    public String cancelOrder(String emailId, int orderId) {
        log.info("Inside cancelOrder method");

        // Retrieve the user entity
        User user = userRepo.findByEmailId(emailId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Retrieve the order to cancel
        Orders order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Set the order as inactive (canceled)
        order.setActive(false);
        orderRepo.save(order);

        // Send an order cancellation email
        String subject = "Order Cancelled Successfully";
        sendOrderCancelMail(emailId, orderId, subject, javaMailSender);

        return "Order Cancelled Successfully";
    }




}
