package com.robot.hotel.email;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Getter
@RequiredArgsConstructor
public enum EmailSubject {
    CONFIRM_EMAIL("Email confirmation") {
        @Override
        public String getTemplate() {
            return TEMPLATE_FOR_CONFIRM_EMAIL;
        }

        @Override
        public String getLink(String token) {
            return backendBaseUrl + TOKEN_CONFIRMATION_EMAIL_URL + token;
        }
    },
    FORGOT_PASSWORD("Reset password instruction") {
        @Override
        public String getTemplate() {
            return TEMPLATE_FOR_FORGOT_PASSWORD_EMAIL;
        }

        @Override
        public String getLink(String token) {
            return backendBaseUrl + TOKEN_RESET_PASSWORD_URL + token;
        }
    },
    RESERVATION_CONFIRMATION("Reservation confirmation") {
        @Override
        public String getTemplate() {
            return TEMPLATE_FOR_RESERVATION_CONFIRMATION;
        }
    },
    RESERVATION_REMINDER("Reservation reminder") {
        @Override
        public String getTemplate() {
            return TEMPLATE_FOR_RESERVATION_REMINDER;
        }
    };

    private final String subject;

    @Value("${backend.base.url}")
    private static String backendBaseUrl;

    private static final String TOKEN_CONFIRMATION_EMAIL_URL = "/api/v1/auth/confirm?token=";
    private static final String TOKEN_RESET_PASSWORD_URL = "/api/v1/users/reset-password?token=";
    private static final String TEMPLATE_FOR_CONFIRM_EMAIL = "confirm-email";
    private static final String TEMPLATE_FOR_FORGOT_PASSWORD_EMAIL = "forgot-password-email";
    private static final String TEMPLATE_FOR_RESERVATION_CONFIRMATION = "reservation-confirmation";
    private static final String TEMPLATE_FOR_RESERVATION_REMINDER = "reservation-reminder";

    public abstract String getTemplate();

    public String getLink(String token) {
        return null;
    }
}