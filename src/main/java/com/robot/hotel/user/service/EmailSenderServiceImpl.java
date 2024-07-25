package com.robot.hotel.user.service;

import com.robot.hotel.exception.FailedToSendEmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {
    private final JavaMailSender mailSender;
    private final TemplateEngine htmlTemplateEngine;

    private static final String MAIL_ENCODING = "utf-8";
    private static final String TOKEN_CONFIRMATION_EMAIL_URL = "/auth/confirm?token=";
    private static final String TEMPLATE_FOR_CONFIRM_EMAIL = "confirm-email";

    @Value("${backend.base.url}")
    private String backendBaseUrl;

    @Value("${email.sender.login}")
    private String sender;

    @Override
    @Async
    public void send(String to, String email, String subject) {
        try {
            log.info("Sending letter to {}", to.toLowerCase());

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, MAIL_ENCODING);
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(sender);
            mailSender.send(message);

            log.info("Successful sent letter to {}", to.toLowerCase());
        } catch (MessagingException e) {
            throw new FailedToSendEmailException("Failed to send email " + e);
        }
    }

    @Override
    public String buildEmailContent(String name, String token) {
        String link = backendBaseUrl + TOKEN_CONFIRMATION_EMAIL_URL + token;

        Context ctx = new Context();
        ctx.setVariable("name", name);
        ctx.setVariable("url", link);
        return htmlTemplateEngine.process(TEMPLATE_FOR_CONFIRM_EMAIL, ctx);
    }
}
