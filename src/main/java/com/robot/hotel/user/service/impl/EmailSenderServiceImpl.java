package com.robot.hotel.user.service.impl;

import com.robot.hotel.exception.FailedToSendEmailException;
import com.robot.hotel.user.service.EmailSenderService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {
    private final JavaMailSender mailSender;

    private static final String MAIL_ENCODING = "utf-8";

    @Value("${email.sender.login}")
    private String sender;

    //TODO add test
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
}
