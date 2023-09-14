package com.ekart.serviceImpl;

import com.ekart.dto.request.GuestLoginDto;
import com.ekart.dto.request.UserLoginDto;
import com.ekart.dto.request.UserRegisterDto;
import com.ekart.dto.response.AuthenticationResponse;
import com.ekart.exception.EntityAlreadyExistException;
import com.ekart.exception.InvalidCredentials;
import com.ekart.jwt.JwtService;
import com.ekart.model.Account;
import com.ekart.model.RoleType;
import com.ekart.model.User;
import com.ekart.repo.UserRepo;
import com.ekart.service.AuthService;
import com.ekart.token.Token;
import com.ekart.token.TokenRepository;
import com.ekart.token.TokenType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public int register(@Valid UserRegisterDto userRegisterDto) throws EntityAlreadyExistException {
        log.info("Inside Register method");
        if (userRepo.existsByEmailId(userRegisterDto.getEmailId())) {
            throw new EntityAlreadyExistException("User already register with given emailId");
        }
        Account account = Account.builder()
                .openingDate(LocalDateTime.now())
                .build();

        User user = User.builder()
                .firstName(userRegisterDto.getFirstName().toUpperCase())
                .lastName(userRegisterDto.getLastName().toUpperCase())
                .emailId(userRegisterDto.getEmailId().toUpperCase())
                .contactNumber(userRegisterDto.getContactNumber())
                .gender(userRegisterDto.getGender())
                .account(account)
                .password(passwordEncoder.encode(userRegisterDto.getPassword()))
                .registrationDate(LocalDateTime.now())
                .role(RoleType.USER)
                .build();

        User registerUser = userRepo.save(user);
        return registerUser.getUserId();
    }

    @Override
    public AuthenticationResponse authenticate(UserLoginDto userLoginDto) throws InvalidCredentials {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginDto.getEmailId(),
                            userLoginDto.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            // Handle invalid credentials here, for example, you can log the error.
            // You can also throw a custom exception or return an error response.
            throw new InvalidCredentials("Invalid credentials provided.");
        }
        User user = userRepo.findByEmailId(userLoginDto.getEmailId())
                .orElseThrow(ResourceNotFoundException::new);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponse guestLogin(GuestLoginDto guestLoginDto) {
        return AuthenticationResponse.builder()
                .build();
    }


    public void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUserId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
