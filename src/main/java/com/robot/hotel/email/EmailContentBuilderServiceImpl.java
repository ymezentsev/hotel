package com.robot.hotel.email;

import com.robot.hotel.reservation.Reservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailContentBuilderServiceImpl implements EmailContentBuilderService {
    private final TemplateEngine htmlTemplateEngine;

    private static final String TOKEN_CONFIRMATION_EMAIL_URL = "/api/v1/auth/confirm?token=";
    private static final String TOKEN_RESET_PASSWORD_URL = "/api/v1/users/reset-password?token=";
    private static final String TEMPLATE_FOR_CONFIRM_EMAIL = "confirm-email";
    private static final String TEMPLATE_FOR_FORGOT_PASSWORD_EMAIL = "forgot-password-email";
    private static final String TEMPLATE_FOR_RESERVATION_CONFIRMATION = "reservation-confirmation";
    private static final String TEMPLATE_FOR_RESERVATION_REMINDER = "reservation-reminder";

    @Value("${backend.base.url}")
    private String backendBaseUrl;

    @Override
    public String buildEmailContent(String name, String token, Reservation reservation, EmailSubject subject) {
        String link = null;
        String template;
        switch (subject) {
            case CONFIRM_EMAIL -> {
                link = backendBaseUrl + TOKEN_CONFIRMATION_EMAIL_URL + token;
                template = TEMPLATE_FOR_CONFIRM_EMAIL;
            }
            case FORGOT_PASSWORD -> {
                link = backendBaseUrl + TOKEN_RESET_PASSWORD_URL + token;
                template = TEMPLATE_FOR_FORGOT_PASSWORD_EMAIL;
            }
            case RESERVATION_CONFIRMATION -> template = TEMPLATE_FOR_RESERVATION_CONFIRMATION;
            case RESERVATION_REMINDER -> template = TEMPLATE_FOR_RESERVATION_REMINDER;
            default -> throw new IllegalArgumentException("Wrong email subject");
        }

        Context ctx = new Context();
        ctx.setVariable("name", name);
        if (token != null) {
            ctx.setVariable("url", link);
        }

        if (reservation != null) {
            //ctx.setVariable("reservation", reservation);
        }
        return htmlTemplateEngine.process(template, ctx);
    }
}
