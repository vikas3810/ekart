package com.ekart.controller;

import com.ekart.dto.request.ForgotPasswordDto;
import com.ekart.dto.response.ApiResponse;
import com.ekart.exception.UnAuthorizedUserException;
import com.ekart.jwt.JwtService;
import com.ekart.model.User;
import com.ekart.service.UserService;
import com.ekart.util.AuthenticationHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ekart.util.AuthenticationHelper.getEmailFromJwt;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    /**
     * Get user details based on JWT token.
     *
     * @param request The HTTP servlet request.
     * @return ResponseEntity containing user details.
     * @throws UnAuthorizedUserException If the user is not authorized.
     */
    @GetMapping("/getUser")
    public ResponseEntity<ApiResponse> getUser(HttpServletRequest request) throws UnAuthorizedUserException {
        log.info("getUser called in user controller");
        String emailId = getEmailFromJwt(request);
        AuthenticationHelper.compareJwtEmailIdAndCustomerEmailId(request, jwtService, emailId);
        User result = userService.getUserByEmailId(emailId);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "Retrieve Successfully", result);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Get user details by user ID.
     *
     * @param userId  The user's ID.
     * @param request The HTTP servlet request.
     * @return ResponseEntity containing user details.
     * @throws UnAuthorizedUserException If the user is not authorized.
     */
    @GetMapping("/getUserByUserId/{userId}")
    public ResponseEntity<ApiResponse> getUserByUserId(@PathVariable Integer userId,
                                                       HttpServletRequest request) throws UnAuthorizedUserException {
        log.info("getUserByUserId called in user controller");
        String emailId = getEmailFromJwt(request);
        AuthenticationHelper.compareJwtEmailIdAndCustomerEmailId(request, jwtService, emailId);
        User result = userService.getUserByUserId(emailId, userId);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "Retrieve Successfully", result);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Update user password.
     *
     * @param newPassword The new password.
     * @param request     The HTTP servlet request.
     * @return ResponseEntity with a success message.
     * @throws UnAuthorizedUserException If the user is not authorized.
     */
    @PostMapping("/updatePassword/{newPassword}")
    public ResponseEntity<ApiResponse> updatePassword(@PathVariable String newPassword,
                                                      HttpServletRequest request) throws UnAuthorizedUserException {
        log.info("updatePassword called in user controller");
        String emailId = getEmailFromJwt(request);
        AuthenticationHelper.compareJwtEmailIdAndCustomerEmailId(request, jwtService, emailId);
        String result = userService.updatePassword(emailId, newPassword);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "Password updated Successfully", result);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Send OTP for password reset.
     *
     * @param session The HTTP session.
     * @param emailId The email ID for which OTP is sent.
     * @return ResponseEntity with a success message.
     *
     */
    @PostMapping("/forgotPasswordSendOTP/{emailId}")
    public ResponseEntity<ApiResponse> forgotPasswordSendOTP(HttpSession session,
                                                             @PathVariable String emailId) {
        log.info("forgotPasswordSendOTP called in user controller");

        String OTP = userService.sendOTP(emailId);
        session.setAttribute("OTP", OTP);
        session.setAttribute("emailId", emailId);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "OTP sent Successfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Verify OTP and update password for password reset.
     *
     * @param OTP                   The OTP to be verified.
     * @param emailId               The email ID for which OTP is verified.
     * @param forgotPasswordDto     The DTO containing new password.
     * @return ResponseEntity with a success message or error message.
     *
     */
    @PostMapping("/forgotPasswordVerifyOTP")
    public ResponseEntity<ApiResponse> forgotPasswordVerifyOTP(@SessionAttribute("OTP") String OTP,
                                                               @SessionAttribute("emailId") String emailId,
                                                               @RequestBody ForgotPasswordDto forgotPasswordDto) {
        log.info("forgotPasswordVerifyOTP called in user controller");

        if (emailId.equals(forgotPasswordDto.getEmailId()) && OTP.equals(forgotPasswordDto.getOtp().toString())) {
            log.info("emailId and OTP match");
            userService.verifyOTPAndUpdatePassword(forgotPasswordDto);
            ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "Password reset Successfully");
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } else {
            log.info("emailId or OTP mismatch");
            ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST.value(), "Email ID or OTP mismatch");
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
