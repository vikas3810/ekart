package com.ekart.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {
    @NotBlank(message = "emailId should not be blank")
    @NotNull(message="emailId should not be null")
    @Email(message ="not valid email format ")
    private String emailId;

    @NotBlank(message = "password should not be blank")
    @NotNull(message="password should not be null")
    private String  password;
}
