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

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    @Id
    @GeneratedValue
    private int addressId;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String pinCode;

    @ManyToOne()
//    @JsonIgnoreProperties("address")
    @JsonIgnore
    private User user;
}
