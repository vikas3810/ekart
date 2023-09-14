package com.ekart.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class OTPGenerator {
    private static final String ALLOWED_CHARS = "0123456789";
    private static final int OTP_LENGTH = 6;
    private static final Random RANDOM = new Random();
    private static final Map<String, String> otpMap = new ConcurrentHashMap<>();


    public static String generateOTP(String emailId) {
        StringBuilder OTP = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            int randomIndex = RANDOM.nextInt(ALLOWED_CHARS.length());
            char randomChar = ALLOWED_CHARS.charAt(randomIndex);
            OTP.append(randomChar);
        }
        otpMap.put(emailId,OTP.toString());
        return OTP.toString();
    }

    public static boolean verifyOTP(String emailId, String enteredOTP) {
        String storedOTP = otpMap.get(emailId);
        if(storedOTP==null){
            throw new BadCredentialsException("OTP expired");
        }
        return storedOTP != null && storedOTP.equals(enteredOTP);
    }


}
