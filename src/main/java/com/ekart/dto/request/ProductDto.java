package com.ekart.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    @NotNull(message = "serialNumber Name should not be null")
    private String serialNumber;
    @NotNull(message = "productName Name should not be null")
    private String productName;
    @NotNull(message = "slug should not be null")
    private String slug;
    @NotNull(message = "categoryId should not be null")
    private int categoryId;
    @NotNull(message = "description should not be null")
    private String description;
    @NotNull(message = "description should not be null")
    private double price;
    @NotNull(message = "description should not be null")
    private int quantity;

}
