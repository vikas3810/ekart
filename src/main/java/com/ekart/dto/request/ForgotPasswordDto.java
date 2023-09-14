package com.ekart.dto.request;

import com.ekart.model.CategoryType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordDto {
    @NotNull(message = "emailId should not be null")
    private String emailId;
    @NotNull(message = "otp should not be null")
    private Integer otp;
    @NotNull(message = "newPassword should not be null")
    private String newPassword;
}
