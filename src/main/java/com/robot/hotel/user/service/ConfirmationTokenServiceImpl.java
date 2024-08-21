package com.robot.hotel.user.service;

import com.robot.hotel.exception.TokenAlreadyConfirmedException;
import com.robot.hotel.exception.TokenExpiredException;
import com.robot.hotel.user.model.ConfirmationToken;
import com.robot.hotel.user.repository.ConfirmationTokenRepository;
import com.robot.hotel.user.model.User;
import com.robot.hotel.user.service.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;

    private static final String TOKEN_NOT_FOUND = "Token %s not found";
    private static final String EMAIL_ALREADY_CONFIRMED = "Email %s is already confirmed";
    private static final String TOKEN_EXPIRED = "Token %s has expired";

    @Value("${email.confirmation.token.lifetime}")
    private Long tokenLifetime;

    @Override
    public String saveConfirmationToken(ConfirmationToken token) {
        return confirmationTokenRepository.save(token).getToken();
    }

    @Override
    public ConfirmationToken getConfirmationToken(String token) {
        return confirmationTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new NoSuchElementException(String.format(TOKEN_NOT_FOUND, token)));
    }

    @Override
    public ConfirmationToken generateConfirmationToken(User user) {
        ConfirmationToken token = ConfirmationToken.builder()
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(tokenLifetime))
                .user(user)
                .build();

        log.info("Confirmation token for user with email: {} has successful generated", user.getEmail());
        return token;
    }

    @Override
    public void validateConfirmationToken(ConfirmationToken confirmationToken) {
        if (confirmationToken.getConfirmedAt() != null) {
            throw new TokenAlreadyConfirmedException(String.format(EMAIL_ALREADY_CONFIRMED,
                    confirmationToken.getUser().getEmail()));
        }

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException(String.format(TOKEN_EXPIRED, confirmationToken.getToken()));
        }
        log.info("Confirmation token - {} has successful validated", confirmationToken.getToken());
    }
}
