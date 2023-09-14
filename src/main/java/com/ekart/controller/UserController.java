package com.ekart.controller;

import com.ekart.dto.request.ForgotPasswordDto;
import com.ekart.dto.response.ApiResponse;
import com.ekart.exception.UnAuthorizedUserException;
import com.ekart.jwt.JwtService;
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

    @PostMapping("/updatePassword/{newPassword}")
    public ResponseEntity<ApiResponse> updatePassword(@PathVariable String newPassword,
                                            HttpServletRequest request) throws UnAuthorizedUserException {
        log.info("updatePassword called in user controller");
        String emailId = getEmailFromJwt(request);
        AuthenticationHelper.compareJwtEmailIdAndCustomerEmailId(request, jwtService,emailId);
        String result = userService.updatePassword(emailId,newPassword);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "Password updated Successfully", result);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


    @PostMapping("/forgotPasswordSendOTP/{emailId}")
    public ResponseEntity<ApiResponse> forgotPasswordSendOTP(HttpSession session,
           @PathVariable String emailId
    ) throws UnAuthorizedUserException {
        log.info("forgotPasswordSendOTP called in user controller");

        String OTP = userService.sendOTP(emailId);
        session.setAttribute("OTP",OTP);
        session.setAttribute("emailId",emailId);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "OTP send Successfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }

    @PostMapping("/forgotPasswordVerifyOTP")
    public ResponseEntity<ApiResponse> forgotPasswordVerifyOTP(@SessionAttribute("OTP") String OTP,
                                                               @SessionAttribute("emailId") String emailId,
            @RequestBody ForgotPasswordDto forgotPasswordDto
            ) throws UnAuthorizedUserException {
        log.info("forgotPasswordVerifyOTP called in user controller");

        if(emailId.equals(forgotPasswordDto.getEmailId()) && OTP.equals(forgotPasswordDto.getOtp().toString())){
            log.info("emailId or otp match");
            userService.verifyOTPAndUpdatePassword(forgotPasswordDto);
            ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "Password reset Successfully");
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }
        log.info("emailId or otp miss match");
        ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST.value(), "emailId or otp miss match");
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);

    }
}
