package com.ekart.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Slf4j
@RequiredArgsConstructor
public class JavaMail {

    public static void sendOTPMail(String emailId,String otp,String subject,JavaMailSender javaMailSender) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("${spring.mail.username}");
        message.setTo(emailId);

        message.setText("Hi Dear" + "\n" + "You have requested opt = "+otp +" to "+subject
        );
        message.setSubject(subject);
        javaMailSender.send(message);
        log.info(subject+" email Sent Successfully");
    }
}
