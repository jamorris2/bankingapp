package com.icebank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    public void sendVerificationEmail(String to, String token) {
        String baseUrl = env.getProperty("app.base.url");
        String verificationUrl = baseUrl + "/verify?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Verify your IceBank Account");
        message.setText("Click the link to verify your account: " + verificationUrl);
        message.setFrom(env.getProperty("support.email"));

        mailSender.send(message);
    }

    public void sendResetPasswordEmail(String to, String token) {
        String baseUrl = env.getProperty("app.base.url");
        String resetPasswordUrl = baseUrl + "/changePassword?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Reset Password");
        message.setText("Click the link to reset your password: " + resetPasswordUrl);
        message.setFrom(env.getProperty("support.email"));

        mailSender.send(message);
    }
}