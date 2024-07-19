package com.robot.hotel.user.service;

import com.robot.hotel.user.ConfirmationToken;
import com.robot.hotel.user.ConfirmationTokenRepository;
import com.robot.hotel.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;

    private static final String TOKEN_NOT_FOUND = "Token %s not found";

    @Value("${email.confirmation.token.lifetime}")
    private Long tokenLifetime;

    @Override
    public String saveConfirmationToken(ConfirmationToken token) {
        return confirmationTokenRepository.save(token).getToken();
    }

    @Override
    public ConfirmationToken getToken(String token) {
        return confirmationTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new NoSuchElementException(String.format(TOKEN_NOT_FOUND, token)));
    }

    @Override
    public ConfirmationToken generateToken(User user) {
        return ConfirmationToken.builder()
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(tokenLifetime))
                .user(user)
                .build();
    }
}
