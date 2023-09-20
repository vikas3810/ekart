package com.ekart.serviceImpl;

import com.ekart.dto.request.GuestLoginDto;
import com.ekart.dto.request.UserLoginDto;
import com.ekart.dto.request.UserRegisterDto;
import com.ekart.dto.response.AuthenticationResponse;
import com.ekart.exception.EntityAlreadyExistException;
import com.ekart.exception.InvalidCredentials;
import com.ekart.jwt.JwtService;
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

    /**
     * Registers a new user.
     *
     * @param userRegisterDto The DTO containing user registration information.
     * @return The ID of the registered user.
     * @throws EntityAlreadyExistException if a user with the provided email already exists.
     */
    @Override
    public int register(@Valid UserRegisterDto userRegisterDto) throws EntityAlreadyExistException {
        log.info("Inside Register/SignUp method");
        if (userRepo.existsByEmailId(userRegisterDto.getEmailId())) {
            throw new EntityAlreadyExistException("User already registered with the given emailId");
        }
        User user = User.builder()
                .firstName(userRegisterDto.getFirstName().toUpperCase())
                .lastName(userRegisterDto.getLastName().toUpperCase())
                .emailId(userRegisterDto.getEmailId().toLowerCase())
                .contactNumber(userRegisterDto.getContactNumber())
                .gender(userRegisterDto.getGender())

                .password(passwordEncoder.encode(userRegisterDto.getPassword()))
                .registrationDate(LocalDateTime.now())
                .role(RoleType.USER)
                .build();

        User registeredUser = userRepo.save(user);
        return registeredUser.getUserId();
    }

    /**
     * Authenticates a user based on login credentials.
     *
     * @param userLoginDto The DTO containing user login credentials.
     * @return An authentication response containing access and refresh tokens.
     * @throws InvalidCredentials if the provided credentials are invalid.
     */
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

    /**
     * Performs a guest login.
     *
     * @param guestLoginDto The DTO containing guest login information.
     * @return An authentication response for the guest.
     */
    @Override
    public AuthenticationResponse guestLogin(GuestLoginDto guestLoginDto) {
        return AuthenticationResponse.builder()
                .build();
    }

    /**
     * Saves a user token in the token repository.
     *
     * @param user     The user for whom the token is saved.
     * @param jwtToken The JWT token to be saved.
     */
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

    /**
     * Revokes all valid user tokens.
     *
     * @param user The user for whom tokens are revoked.
     */
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
