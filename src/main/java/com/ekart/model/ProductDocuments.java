package com.ekart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ProductDocuments implements Serializable {
    @Id
    @GeneratedValue
    private int productDocumentId;
    @ManyToOne
    @JsonIgnore
    private Product product;
    //    @Enumerated(EnumType.STRING)
//    private DocumentType documentType;
    private String documentFormat;
    private String path;
    private boolean isActive;
    private String createdBy;
    private LocalDate createdDate;
    private String modifiedBy;
    private LocalDate modifiedDate;
}
