package com.ekart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class Category {
    @Id
    @GeneratedValue
    private int categoryId;
    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;
    private String slug;
    private String description;
    private String createdBy;
    private LocalDateTime createdDate;
    private String modifiedBy;
    private LocalDateTime modifiedDate;
    private boolean isActive;
    @OneToMany(mappedBy = "category",fetch = FetchType.EAGER)
//    @JsonIgnore
    private List<Product> products = new ArrayList<>();
}
