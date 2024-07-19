package com.robot.hotel.user.service;

import com.robot.hotel.exception.ConfirmationTokenException;
import com.robot.hotel.user.model.ConfirmationToken;
import com.robot.hotel.user.repository.ConfirmationTokenRepository;
import com.robot.hotel.user.model.User;
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
        return ConfirmationToken.builder()
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(tokenLifetime))
                .user(user)
                .build();
    }

    @Override
    public void validateConfirmationToken(String token) {
        ConfirmationToken confirmationToken = getConfirmationToken(token);

        if (confirmationToken.getConfirmedAt() != null) {
            throw new ConfirmationTokenException(String.format(EMAIL_ALREADY_CONFIRMED,
                    confirmationToken.getUser().getEmail()));
        }

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ConfirmationTokenException(String.format(TOKEN_EXPIRED, token));
        }
    }
}
