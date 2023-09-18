package com.ekart.util;

import com.ekart.exception.UnAuthorizedUserException;
import com.ekart.jwt.JwtService;
import com.ekart.model.User;
import com.ekart.repo.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthenticationHelper {

    public static void compareJwtEmailIdAndCustomerId(HttpServletRequest request, JwtService jwtService,
                                                      UserRepo userRepo, int userId) throws UnAuthorizedUserException {
        log.info("compareJwtEmailIdAndCustomerId");
        String jwt = request.getHeader("Authorization").substring(7);
        String jwtEmail = jwtService.extractEmailId(jwt);
        User user = userRepo.findById(userId).orElseThrow(ResourceNotFoundException::new);
        // Compare the extracted jwt username with the customer EmailId from the path variable
        //            throw new UnAuthorizedUserException("UnAuthorized User");
        if( !jwtEmail.equals(user.getEmailId())){
            throw new UnAuthorizedUserException("Unauthorized user");
        }
    }

    public static void compareJwtEmailIdAndCustomerEmailId(HttpServletRequest request, JwtService jwtService,
                                                            String emailId) throws UnAuthorizedUserException {
        log.info("compareJwtEmailIdAndCustomerEmailId");
        String jwt = request.getHeader("Authorization").substring(7);
        String jwtEmail = jwtService.extractEmailId(jwt);

        // Compare the extracted jwt username with the customer EmailId from the path variable
        //            throw new UnAuthorizedUserException("UnAuthorized User");
        if( !jwtEmail.equals(emailId)){
            throw new UnAuthorizedUserException("Unauthorized user");
        }
    }

public static void adminAuthorized(String emailId) throws UnAuthorizedUserException {

        if(!emailId.contains("@ekart")){
            throw new UnAuthorizedUserException("UnAuthorized user only admin can do");
        }
    }
    public static String getName(HttpServletRequest request, HttpServletResponse response) {
                return request.getRequestedSessionId();
    }

    public static String getEmailFromJwt(HttpServletRequest request) {
        String jwt = request.getHeader("Authorization").substring(7);
        return request.getUserPrincipal().getName();
    }
}
