package com.ekart.service;

import com.ekart.dto.request.ForgotPasswordDto;
import jakarta.validation.Valid;

public interface UserService {
    //signup method in authService
    //signIn /Login in authService

    String updatePassword(String emailId,String newPassword);
    String sendOTP(String emailId);
    String verifyOTPAndUpdatePassword(@Valid ForgotPasswordDto forgotPasswordDto);
}
