package com.ekart.jwt;


import com.ekart.token.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        log.info("doFilterInternal called");

        String servletPath = request.getServletPath();

        if (
                servletPath.contains("/auth/authenticate")||
                servletPath.contains("/auth/register")||
                servletPath.contains("/swagger-ui")||
                        servletPath.contains("/actuator")||
                        servletPath.contains("/v2/api-docs")||
                        servletPath.contains("/v3/api-docs")

                ||servletPath.contains("/controller")
                        ||servletPath.contains("/user/forgotPasswordSendOTP")
                        ||servletPath.contains("/user/forgotPasswordVerifyOTP")
                        ||servletPath.contains("/auth/guest")

        ) {
            filterChain.doFilter(request, response);
            log.info("............By pass..............");
            return;
        }

        final String authHeader = request.getHeader("Authorization");
//        System.out.println(authHeader);
        final String jwt;
        final String emailId;

           if (authHeader == null || !authHeader.startsWith("Bearer ")) {
           log.info("JWT is null");
               response.setStatus(HttpStatus.UNAUTHORIZED.value());
               response.getWriter().write("UNAUTHORIZED");
               return;
           }

        //Extract  JWT from authHeader
        jwt = authHeader.substring(7);
           //validated provided token
        var isTokenValid = tokenRepository.findByToken(jwt)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);
//token not valid throw UnauthorizedException and msg 'Token is expired'
        if (!isTokenValid) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Token is expired");
            return;
        }
        //if token valid
        //extract emailId from jwt
        emailId = jwtService.extractEmailId(jwt);
        if (emailId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(emailId);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new
                        UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request,response);

    }


}

