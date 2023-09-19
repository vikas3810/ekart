package com.ekart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDocuments implements Serializable {
    @Id
    @GeneratedValue
    private int categoryDocumentId;
    @ManyToOne
    @JsonIgnore
    private Category category;
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
