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
public class Orders {
    @Id
    @GeneratedValue
    private int orderId;

    private String createdBy;
    private LocalDateTime createdDate;
    private String modifiedBy;
    private LocalDateTime modifiedDate;
    private boolean isActive;
    @ManyToOne
    private User user;
    @ManyToMany
    private List<Product> product = new ArrayList<>();
}
