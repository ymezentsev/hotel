package com.robot.hotel.user.service;

import com.robot.hotel.user.model.enums.EmailSubject;

public interface EmailContentBuilderService {
    String buildEmailContent(String name, String token, EmailSubject subject);
}
