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

        public AuthenticationResponse adminRegister(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .emailId(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(RoleType.ADMIN)
                .build();
        var savedUser = userRepo.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
//        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody @Valid UserRegisterDto request
    ) throws EntityAlreadyExistException {
        log.info("Register called in Auth controller ");

        int authenticationResponse = authService.register(request);
        ApiResponse apiresponse = new ApiResponse(HttpStatus.CREATED.value(), "user is created successfully", authenticationResponse);
        return new ResponseEntity<>(apiresponse, HttpStatus.OK);
    }


    @PostMapping("/authenticate")

    public ResponseEntity<ApiResponse> auth(@RequestBody  UserLoginDto userLoginDto) throws InvalidCredentials {
        log.info("login called in Auth controller ");
        AuthenticationResponse authenticationResponse = authService.authenticate(userLoginDto);
        ApiResponse apiresponse = new ApiResponse(HttpStatus.OK.value(), "user is validated ", authenticationResponse);
        return new ResponseEntity<>(apiresponse, HttpStatus.OK);
    }


    @PostMapping("/guest/login")

    public ResponseEntity<GuestApiResponse> guest(@RequestBody @Valid GuestLoginDto guestLoginDto) {
        log.info("Guest login called in Auth controller ");
        AuthenticationResponse authenticationResponse = authService.guestLogin(guestLoginDto);
        ApiResponse apiresponse = new ApiResponse(HttpStatus.OK.value(), "Guest user is validated ", authenticationResponse);
        GuestApiResponse response = new GuestApiResponse(true, "Guest user is validated ",authenticationResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
