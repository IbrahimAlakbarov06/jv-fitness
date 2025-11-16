package org.jvfitness.service;

import lombok.RequiredArgsConstructor;
import org.jvfitness.domain.entity.User;
import org.jvfitness.domain.repository.UserRepository;
import org.jvfitness.exception.*;
import org.jvfitness.mapper.UserMapper;
import org.jvfitness.model.dto.request.*;
import org.jvfitness.model.dto.response.AuthResponse;
import org.jvfitness.model.dto.response.MessageResponse;
import org.jvfitness.model.enums.Role;
import org.jvfitness.model.enums.UserStatus;
import org.jvfitness.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final RedisService redisService;

    private static final Random RANDOM = new SecureRandom();
    private static final int VERIFICATION_CODE_LENGTH = 6;

    @Transactional
    public MessageResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }

        if (request.getPhoneNumber() != null && userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new AlreadyExistsException("User with phone number " + request.getPhoneNumber() + " already exists");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_USER);
        user.setStatus(UserStatus.UNVERIFIED);

        userRepository.save(user);

        String verificationCode = generateVerificationCode();
        redisService.saveVerificationCode(request.getEmail(), verificationCode);

        emailService.sendVerificationEmail(request.getEmail(), verificationCode);

        return new MessageResponse("Registration successful! Please check your email for verification code.");
    }

    @Transactional
    public AuthResponse verifyUser(VerifyEmailRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User with email: " + request.getEmail() + " not found"));

        if (user.getStatus() == UserStatus.ACTIVE) {
            throw new EmailException("Email is already verified");
        }

        if (!redisService.verifyCode(user.getEmail(), request.getCode())) {
            throw new EmailException("Invalid or expired verification code");
        }

        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        redisService.deleteVerificationCode(user.getEmail());

        emailService.sendWelcomeEmail(user.getEmail(), user.getFullName());

        String accessToken = jwtService.generateToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtService.getExpirationTime())
                .user(userMapper.toUserResponse(user))
                .build();
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User with email: " + request.getEmail() + " not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new NotFoundException("Invalid email or password");
        }

        if (user.getStatus() == UserStatus.UNVERIFIED) {
            throw new EmailException("Please verify your email before logging in");
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessException("User is not active");
        }

        String accessToken = jwtService.generateToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtService.getExpirationTime())
                .user(userMapper.toUserResponse(user))
                .build();
    }

    public MessageResponse logout(String token) {
        try {
            long expirationTime = jwtService.extractExpiration(token).getTime() - System.currentTimeMillis();

            if (expirationTime > 0) {
                redisService.blacklistToken(token, expirationTime);
                return new MessageResponse("Logged out successfully");
            } else {
                return new MessageResponse("Token already expired");
            }
        } catch (Exception e) {
            throw new InvalidInputException("Invalid token");
        }
    }

    @Transactional(readOnly = true)
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        try {
            if (redisService.isBlacklisted(request.getRefreshToken())) {
                throw new IllegalArgumentException("Token has been revoked");
            }

            String email = jwtService.extractUsername(request.getRefreshToken());

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException("User not found"));

            if (!jwtService.validateToken(request.getRefreshToken(), user.getEmail())) {
                throw new IllegalArgumentException("Invalid refresh token");
            }

            String accessToken = jwtService.generateToken(user.getEmail());
            String refreshToken = jwtService.generateRefreshToken(user.getEmail());


            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .expiresIn(jwtService.getExpirationTime())
                    .user(userMapper.toUserResponse(user))
                    .build();
        } catch (Exception e) {
            throw new InvalidInputException("Invalid refresh token");
        }
    }

    @Transactional(readOnly = true)
    public MessageResponse resendVerificationCode(ResendVerificationRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User with email: " + request.getEmail() + " not found"));

        if (user.getStatus() == UserStatus.ACTIVE) {
            throw new EmailException("Email is already verified");
        }

        String verificationCode = generateVerificationCode();
        redisService.saveVerificationCode(user.getEmail(), verificationCode);

        emailService.sendVerificationEmail(request.getEmail(), verificationCode);
        return new MessageResponse("Resend verification code successful.");
    }

    @Transactional(readOnly = true)
    public MessageResponse forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found with email: " + request.getEmail()));

        if (user.getStatus() == UserStatus.UNVERIFIED) {
            throw new IllegalStateException("Please verify your email first");
        }

        String resetCode = generateVerificationCode();
        redisService.savePasswordResetCode(request.getEmail(), resetCode);

        emailService.sendPasswordResetEmail(request.getEmail(), resetCode);

        return new MessageResponse("Password reset code has been sent to your email.");
    }

    @Transactional
    public MessageResponse resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found with email: " + request.getEmail()));

        if (!redisService.verifyPasswordResetCode(request.getEmail(), request.getCode())) {
            throw new EmailException("Reset password code is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        redisService.deletePasswordResetCode(request.getEmail());

        return new MessageResponse("Password has been reset successfully. You can now login with your new password.");
    }

    private String generateVerificationCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < VERIFICATION_CODE_LENGTH; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }
}
