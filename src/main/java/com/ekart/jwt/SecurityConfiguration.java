package com.ekart.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
@EnableMethodSecurity
@EnableWebMvc
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("securityFilterChain called in SecurityConfiguration");
        http

                .csrf().disable();
        http
                .authorizeHttpRequests()
                //api containing following strings will be byPass by authentication
                .requestMatchers( "/auth/authenticate/**",
                        "/auth/register",
                        "/controller/**",
                        "/user/forgotPasswordSendOTP/**",
                        "/user/forgotPasswordVerifyOTP/**",
                        "/v2/api-docs",
                        "/v2/api-docs/**",
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/accounts/**",
                        "/swagger-resources/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/swagger-ui/**",
                        "/actuator/**",
                        "/webjars/**",
                        "/auth/guest/**",
                        "/swagger-ui.html")
                .permitAll()

                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

      return http.build();
    }
}

