package org.jvfitness.service;

import lombok.RequiredArgsConstructor;
import org.jvfitness.exception.EmailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("JV Fitness - Email Verification");
            message.setText(String.format(
                    "Welcome to JV Fitness!\n\n" +
                            "Your verification code is: %s\n\n" +
                            "This code will expire in 10 minutes.\n\n" +
                            "If you didn't request this code, please ignore this email.\n\n" +
                            "Best regards,\n" +
                            "JV Fitness Team",
                    code
            ));

            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailException("Failed to send verification email");
        }
    }

    public void sendPasswordResetEmail(String to, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("JV Fitness - Password Reset");
            message.setText(String.format(
                    "Hello,\n\n" +
                            "You have requested to reset your password.\n\n" +
                            "Your password reset code is: %s\n\n" +
                            "This code will expire in 15 minutes.\n\n" +
                            "If you didn't request this code, please ignore this email and your password will remain unchanged.\n\n" +
                            "Best regards,\n" +
                            "JV Fitness Team",
                    code
            ));

            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailException("Failed to send password reset email");
        }
    }

    public void sendWelcomeEmail(String to, String fullName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Welcome to JV Fitness!");
            message.setText(String.format(
                    "Hello %s,\n\n" +
                            "Welcome to JV Fitness! Your account has been successfully verified.\n\n" +
                            "You can now log in and start your fitness journey with us.\n\n" +
                            "Best regards,\n" +
                            "JV Fitness Team",
                    fullName
            ));

            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailException("Failed to send welcome email");
        }
    }
}