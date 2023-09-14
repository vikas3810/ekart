package com.ekart.dto.request;

import com.ekart.model.Gender;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {
    @NotNull(message = "First name should not be null")
    @Size(min = 2, max = 60, message = "Name should be between 2 to 60")
    public String firstName;

    @NotNull(message = "Last name should not be null")
    private String lastName;

    @NotNull(message = "emailId should not be null")
    @Email(message = "not valid email format")
    @Column(unique = true)
    private String emailId;

    @NotNull(message = "contactNo  should not be null")
    @Pattern(regexp = "^\\d{10}$")
    private String contactNumber;

    @NotNull(message = "Gender should not be null")
    private Gender gender;

    @NotNull(message = "password should not be null")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")
    private String password;

    @NotNull(message = "confirmPassword should not be null")
    private String confirmPassword;

}
