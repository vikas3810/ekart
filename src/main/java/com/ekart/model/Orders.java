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
public class Orders implements Serializable {
    @Id
    @GeneratedValue
    private int orderId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private boolean isActive;
    @ManyToOne
    private User user;
    @ManyToMany
    private List<CartProduct> product = new ArrayList<>();
}
