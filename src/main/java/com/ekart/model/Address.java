package com.ekart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address implements Serializable {
    @Id
    @GeneratedValue
    private int addressId;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String pinCode;

    @ManyToOne()
    @JsonIgnoreProperties("addresses")
    @JsonIgnore
    private User user;
    @Override
    public String toString() {
        return "Address{" +
                "addressId=" + addressId +
                ", addressLine1='" + addressLine1 + '\'' +
                ", addressLine2='" + addressLine2 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", pinCode='" + pinCode + '\'' +
                '}';
    }
}
