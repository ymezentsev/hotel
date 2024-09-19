package com.robot.hotel.email;

public interface EmailContentBuilderService {
    String buildEmailContent(String name, String token, EmailSubject subject);
}
