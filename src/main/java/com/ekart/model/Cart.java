package com.ekart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart {
    @Id
    @GeneratedValue
    private int cartId;
    private boolean isActive;
    private double subTotal;
    @OneToOne
    private User user;
    @ManyToMany
    private List<CartProduct> cartProducts = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
