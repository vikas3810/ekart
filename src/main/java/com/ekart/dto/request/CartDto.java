package com.ekart.dto.request;

import com.ekart.model.CategoryType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    @NotNull(message = "productId should not be null")
    private Integer productId;

    @NotNull(message = "quantity should not be null")
    private Integer quantity;
}
