package com.ekart.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuestLoginDto {
    @NotBlank(message = "first Name should not be blank")
    @NotNull(message="first Name should not be null")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "Only alphabets are allowed")
    private String firstName;


    @NotBlank(message = "lastName should not be blank")
    @NotNull(message="lastName should not be null")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "Only alphabets are allowed")
    private String  lastName;
}
