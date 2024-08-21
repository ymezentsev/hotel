package com.robot.hotel.user.service.impl;

import com.robot.hotel.user.model.enums.EmailSubject;
import com.robot.hotel.user.service.EmailContentBuilderService;
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

    @Value("${backend.base.url}")
    private String backendBaseUrl;

    @Override
    public String buildEmailContent(String name, String token, EmailSubject subject) {
        String link;
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
            default -> throw new IllegalArgumentException("Wrong email subject");
        }

        Context ctx = new Context();
        ctx.setVariable("name", name);
        ctx.setVariable("url", link);
        return htmlTemplateEngine.process(template, ctx);
    }
}
