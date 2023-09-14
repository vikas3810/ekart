package com.ekart.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
        @NotNull(message = "addressLine1 should not be null")
    private String addressLine1;

    private String addressLine2;

    @NotNull(message = "city name should not be null")
    private String city;

    @NotNull(message = "State name should not be null")
    private String State;

    @NotNull(message = "pinCode should not be null")
    @Size(min = 6, max = 6, message = "pinCode should be between 6 to 6")
    private String pinCode;
}
