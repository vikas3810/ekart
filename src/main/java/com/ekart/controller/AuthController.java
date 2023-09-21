package com.ekart.controller;

import com.ekart.dto.request.GuestLoginDto;
import com.ekart.dto.request.RegisterRequest;
import com.ekart.dto.request.UserLoginDto;
import com.ekart.dto.request.UserRegisterDto;
import com.ekart.dto.response.ApiResponse;
import com.ekart.dto.response.AuthenticationResponse;
import com.ekart.dto.response.GuestApiResponse;
import com.ekart.exception.EntityAlreadyExistException;
import com.ekart.exception.InvalidCredentials;
import com.ekart.jwt.JwtService;
import com.ekart.model.RoleType;
import com.ekart.model.User;
import com.ekart.repo.UserRepo;
import com.ekart.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final JwtService jwtService;

    /**
     * Admin registration endpoint.
     *
     * @param request The registration request.
     * @return AuthenticationResponse with access and refresh tokens.
     */
    @PostMapping("/admin/register")
    public AuthenticationResponse adminRegister(@RequestBody @Valid RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstname().toUpperCase())
                .lastName(request.getLastname().toUpperCase())
                .emailId(request.getEmail().toUpperCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(RoleType.ADMIN)
                .build();
        var savedUser = userRepo.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * User registration endpoint.
     *
     * @param userRegisterDto The user registration request.
     * @return ResponseEntity with ApiResponse indicating success or failure.
     * @throws EntityAlreadyExistException if the user already exists.
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody @Valid UserRegisterDto userRegisterDto
    ) throws EntityAlreadyExistException {
        log.info("Register called in Auth controller ");

        int userId = authService.register(userRegisterDto);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.CREATED.value(), "User is created successfully", userId);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * User authentication endpoint.
     *
     * @param userLoginDto The user login request.
     * @return ResponseEntity with ApiResponse indicating success or failure.
     * @throws InvalidCredentials if the credentials are invalid.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse> authenticate(@RequestBody UserLoginDto userLoginDto) throws InvalidCredentials {
        log.info("Login called in Auth controller ");
        AuthenticationResponse authenticationResponse = authService.authenticate(userLoginDto);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "User is validated", authenticationResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Guest login endpoint.
     *
     * @param guestLoginDto The guest login request.
     * @return ResponseEntity with GuestApiResponse indicating success or failure.
     */
    @PostMapping("/guest/login")
    public ResponseEntity<GuestApiResponse> guest(@RequestBody @Valid GuestLoginDto guestLoginDto) {
        log.info("Guest login called in Auth controller ");
        AuthenticationResponse authenticationResponse = authService.guestLogin(guestLoginDto);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "Guest user is validated", authenticationResponse);
        GuestApiResponse response = new GuestApiResponse(true, "Guest user is validated", authenticationResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
