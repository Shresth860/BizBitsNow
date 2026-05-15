package com.BizBitsNow.BizBitsNow.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    public void sendOtpEmail(String to , String otp){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("BizBitsNow : Verify Your Email");
        message.setText("Your OTP for registration is: " + otp + "\nValid for 5 minutes.");
        javaMailSender.send(message);
    }
}
