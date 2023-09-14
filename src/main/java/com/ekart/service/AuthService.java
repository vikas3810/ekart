package com.ekart.service;

import com.ekart.dto.request.GuestLoginDto;
import com.ekart.dto.request.UserLoginDto;
import com.ekart.dto.request.UserRegisterDto;
import com.ekart.dto.response.AuthenticationResponse;
import com.ekart.exception.EntityAlreadyExistException;
import com.ekart.exception.InvalidCredentials;
import jakarta.validation.Valid;

public interface AuthService {

    int register(@Valid UserRegisterDto userRegisterDto) throws EntityAlreadyExistException;

    AuthenticationResponse authenticate(@Valid UserLoginDto userLoginDto) throws InvalidCredentials;

    AuthenticationResponse guestLogin(@Valid GuestLoginDto guestLoginDto);

}
