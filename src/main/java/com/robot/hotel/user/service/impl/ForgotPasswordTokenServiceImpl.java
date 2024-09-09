package com.robot.hotel.user.service.impl;

import com.robot.hotel.exception.TokenAlreadyConfirmedException;
import com.robot.hotel.exception.TokenExpiredException;
import com.robot.hotel.exception.NoSuchElementException;
import com.robot.hotel.user.model.ForgotPasswordToken;
import com.robot.hotel.user.model.User;
import com.robot.hotel.user.repository.ForgotPasswordTokenRepository;
import com.robot.hotel.user.service.ForgotPasswordTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForgotPasswordTokenServiceImpl implements ForgotPasswordTokenService {
    private final ForgotPasswordTokenRepository forgotPasswordTokenRepository;

    private static final String TOKEN_NOT_FOUND = "Token %s not found";
    private static final String TOKEN_ALREADY_USED = "Token %s has already been used";
    private static final String TOKEN_EXPIRED = "Token %s has expired";

    @Value("${forgot.password.token.lifetime}")
    private Long tokenLifetime;

    @Override
    public String saveForgotPasswordToken(ForgotPasswordToken token) {
        return forgotPasswordTokenRepository.save(token).getToken();
    }

    @Override
    public ForgotPasswordToken getForgotPasswordToken(String token) {
        return forgotPasswordTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new NoSuchElementException(String.format(TOKEN_NOT_FOUND, token)));
    }

    @Override
    public ForgotPasswordToken generateForgotPasswordToken(User user) {
        ForgotPasswordToken token = ForgotPasswordToken.builder()
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(tokenLifetime))
                .user(user)
                .build();

        log.info("Forgot password token for user with email: {} has successful generated", user.getEmail());
        return token;
    }

    @Override
    public void validateForgotPasswordToken(ForgotPasswordToken forgotPasswordToken) {
        if (forgotPasswordToken.getConfirmedAt() != null) {
            throw new TokenAlreadyConfirmedException(String.format(TOKEN_ALREADY_USED,
                    forgotPasswordToken.getUser().getEmail()));
        }

        if (forgotPasswordToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException(String.format(TOKEN_EXPIRED, forgotPasswordToken.getToken()));
        }
        log.info("Forgot password token - {} has successful validated", forgotPasswordToken.getToken());
    }
}
