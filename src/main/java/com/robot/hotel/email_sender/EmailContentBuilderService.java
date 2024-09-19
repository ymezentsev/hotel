package com.robot.hotel.email_sender;

public interface EmailContentBuilderService {
    String buildEmailContent(String name, String token, EmailSubject subject);
}
