package com.ekart.serviceImpl;

import com.ekart.dto.request.ForgotPasswordDto;
import com.ekart.model.User;
import com.ekart.repo.UserRepo;
import com.ekart.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.ekart.util.JavaMail.sendOTPMail;
import static com.ekart.util.OTPGenerator.generateOTP;
import static com.ekart.util.OTPGenerator.verifyOTP;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String updatePassword(String emailId,String newPassword) {
        log.info("inside updatePassword method in service");

        User user = userRepo.findByEmailId(emailId).orElseThrow(ResourceNotFoundException::new);
        user.setPassword(newPassword);
        user.setConfirmPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
        return "password updated";
    }

    @Override
    public String sendOTP(String emailId) {
        log.info("inside forgotPassword method in service");
        User user = userRepo.findByEmailId(emailId).orElseThrow(ResourceNotFoundException::new);
        String otp = generateOTP(emailId);
        String subject = "change password";
        sendOTPMail(emailId,otp,subject,javaMailSender);
        return otp;
    }

    @Override
    public String verifyOTPAndUpdatePassword(@Valid ForgotPasswordDto forgotPasswordDto) {
        log.info("inside verifyOtpAndUpdatePassword method in service");
        User user = userRepo.findByEmailId(forgotPasswordDto.getEmailId()).orElseThrow(ResourceNotFoundException::new);
        user.setPassword(passwordEncoder.encode(forgotPasswordDto.getNewPassword()));
        user.setConfirmPassword(passwordEncoder.encode(forgotPasswordDto.getNewPassword()));
        userRepo.save(user);
        return "Password Reset successfully";
    }

    @Override
    public User getUserByUserId(String emailId, Integer userId) {
        log.info("inside getUserByUserId method in service");

        return userRepo.findById(userId).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public User getUserByEmailId(String emailId) {
        return userRepo.findByEmailId(emailId).orElseThrow(ResourceNotFoundException::new);
    }


}
