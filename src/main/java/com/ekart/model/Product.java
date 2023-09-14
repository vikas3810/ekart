package com.ekart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue
    private int productId;
    private String productName;
    private String slug;
    private double price;
    private int quantity;
    private String description;
    private String serialNumber;
    private boolean isActive;
    private String createdBy;
    private LocalDate createdDate;
    private String modifiedBy;
    private LocalDate modifiedDate;

    @ManyToOne(targetEntity = Category.class,fetch = FetchType.EAGER)
//    @JsonIgnore
    private Category category;
}
