package com.ekart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart implements Serializable {
    @Id
    @GeneratedValue
    private int cartId;
    private boolean isActive;
    private double subTotal;
    @OneToOne
    private User user;
    @ManyToMany
    private List<Product> products = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
