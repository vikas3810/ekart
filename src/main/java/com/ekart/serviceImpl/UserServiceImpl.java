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

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;

    /**
     * Updates the user's password.
     *
     * @param emailId    The email ID of the user.
     * @param newPassword The new password.
     * @return A message indicating that the password was updated.
     */
    @Override
    public String updatePassword(String emailId, String newPassword) {
        log.info("Inside updatePassword method in service");

        // Retrieve the user entity
        User user = userRepo.findByEmailId(emailId)
                .orElseThrow(ResourceNotFoundException::new);

        // Update the password and confirm password
        user.setPassword(newPassword);
        user.setConfirmPassword(passwordEncoder.encode(newPassword));

        // Save the user
        userRepo.save(user);
        return "Password updated";
    }

    /**
     * Sends an OTP to the user's email address for password reset.
     *
     * @param emailId The email ID of the user.
     * @return The OTP sent to the user.
     */
    @Override
    public String sendOTP(String emailId) {
        log.info("Inside sendOTP method in service");

        // Retrieve the user entity
        User user = userRepo.findByEmailId(emailId)
                .orElseThrow(ResourceNotFoundException::new);

        // Generate an OTP
        String otp = generateOTP(emailId);

        // Email subject
        String subject = "Change Password";

        // Send OTP via email
        sendOTPMail(emailId, otp, subject, javaMailSender);

        return otp;
    }

    /**
     * Verifies the OTP and updates the user's password.
     *
     * @param forgotPasswordDto The DTO containing email, OTP, and new password.
     * @return A message indicating a successful password reset.
     */
    @Override
    public String verifyOTPAndUpdatePassword(@Valid ForgotPasswordDto forgotPasswordDto) {
        log.info("Inside verifyOTPAndUpdatePassword method in service");

        // Retrieve the user entity
        User user = userRepo.findByEmailId(forgotPasswordDto.getEmailId())
                .orElseThrow(ResourceNotFoundException::new);

        // Encode and set the new password and confirm password
        user.setPassword(passwordEncoder.encode(forgotPasswordDto.getNewPassword()));
        user.setConfirmPassword(passwordEncoder.encode(forgotPasswordDto.getNewPassword()));

        // Save the updated user
        userRepo.save(user);

        return "Password Reset successfully";
    }

    /**
     * Gets a user by their user ID.
     *
     * @param emailId The email ID of the user.
     * @param userId  The user's ID.
     * @return The user entity.
     */
    @Override
    public User getUserByUserId(String emailId, Integer userId) {
        log.info("Inside getUserByUserId method in service");

        return userRepo.findById(userId).orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Gets a user by their email ID.
     *
     * @param emailId The email ID of the user.
     * @return The user entity.
     */
    @Override
    public User getUserByEmailId(String emailId) {
        return userRepo.findByEmailId(emailId).orElseThrow(ResourceNotFoundException::new);
    }
}
