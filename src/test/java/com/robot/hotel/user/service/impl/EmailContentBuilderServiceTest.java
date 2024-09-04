package com.robot.hotel.user.service.impl;

import com.robot.hotel.ContainerConfiguration;
import com.robot.hotel.user.model.enums.EmailSubject;
import com.robot.hotel.user.service.EmailContentBuilderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)
class EmailContentBuilderServiceTest {
    @Autowired
    EmailContentBuilderService emailContentBuilderService;

    @ParameterizedTest
    @CsvSource(value = {
            "CONFIRM_EMAIL, Verify Email",
            "FORGOT_PASSWORD, Reset password"
    })
    @DisplayName("Successful build email content")
    void buildEmailContentTest(EmailSubject subject, String result) {
        assertTrue(emailContentBuilderService.buildEmailContent("test", "test", subject).contains(result));
    }

    @Test
    @DisplayName("Fail build email content")
    void buildEmailContentThrowIllegalArgumentExceptionTest() {
        assertThrows(IllegalArgumentException.class,
                () -> emailContentBuilderService.buildEmailContent("test", "test", EmailSubject.valueOf("TEST")));
    }
}