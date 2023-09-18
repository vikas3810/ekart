package com.ekart.jwt;

import com.ekart.model.User;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
public class JwtService {

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

//    @Value("${app.jwt.secret}")
    private String SECRET_KEY = "88YxPHTxFF/XtTBxocm+vxYx6OXPRpQPDSfGc0ftb0A=";


    public String extractEmailId(String token) {
        log.info("extractEmailId called in JwtService");
        return extractClaim(token,Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        log.info("extractClaim called in JwtService");
        final Claims claims  = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) throws ExpiredJwtException {
        log.info("extractAllClaims called in JwtService");
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

    }
    public String generateToken(User user){
        log.info("generateToken called in JwtService with customer as parameter");
        return generateToken(new HashMap<>(),user);
    }
    public String generateToken(Map<String,Object> extraClaims, User user) {
        log.info("generateToken called in JwtService");
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getEmailId())
                .setId(String.valueOf(user.getUserId()))
                .setIssuer("eWallet")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(),SignatureAlgorithm.HS256)
                .compact();
    }


    public boolean isTokenValid(String token, UserDetails user){
        log.info("isTokenValid called in JwtService");
        final String username = extractEmailId(token);
        return (username.equals(user.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {

        log.info("isTokenExpired called in JwtService");
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        log.info("extractExpiration called in JwtService");
        return extractClaim(token,Claims::getExpiration);
    }

    private SecretKey getSigningKey() {
        log.info("getSigningKey called in JwtService");
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateRefreshToken(
            UserDetails userDetails
    ) {
        log.info("generateRefreshToken called in JwtService");
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        log.info("buildToken called in JwtService");
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }





//    public  void validateToken(String token) throws JwtExpiredException {
//        try {
//            Claims claims = Jwts.parserBuilder()
//                    .setSigningKey(SECRET_KEY)
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody();
//
//            Date expiration = claims.getExpiration();
//            Date now = new Date();
//
//            // Calculate the difference between the current time and the expiration time in milliseconds
//            long differenceMillis = expiration.getTime() - now.getTime();
//
//            // Define the allowed clock skew in milliseconds (0 milliseconds in your case)
//            long allowedClockSkewMillis = 0;
//
//            if (differenceMillis <= allowedClockSkewMillis) {
//                throw new JwtExpiredException("JWT token has expired");
//            }
//        } catch (Exception e) {
//            throw new JwtExpiredException("JWT token is invalid or expired", e);
//        }
//    }








        }
