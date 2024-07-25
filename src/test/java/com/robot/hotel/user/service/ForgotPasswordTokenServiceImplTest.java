package com.robot.hotel.user.service;

import com.robot.hotel.ContainerConfiguration;
import com.robot.hotel.DBInitializer;
import com.robot.hotel.TestDBUtils;
import com.robot.hotel.exception.TokenAlreadyConfirmedException;
import com.robot.hotel.exception.TokenExpiredException;
import com.robot.hotel.user.model.ForgotPasswordToken;
import com.robot.hotel.user.repository.ForgotPasswordTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)
class ForgotPasswordTokenServiceImplTest {
    @Autowired
    ForgotPasswordTokenService forgotPasswordTokenService;

    @Autowired
    ForgotPasswordTokenRepository forgotPasswordTokenRepository;

    @Autowired
    DBInitializer dbInitializer;

    @Autowired
    TestDBUtils testUtils;

    @BeforeEach
    void setUp() {
        dbInitializer.populateDB();
    }

    @Test
    @DisplayName("Save forgot password token")
    void saveForgotPasswordTokenTest() {
        assertAll(
                () -> assertNotNull(
                        forgotPasswordTokenService.saveForgotPasswordToken(
                                forgotPasswordTokenService.generateForgotPasswordToken(
                                        testUtils.getUserByEmail("sidor@gmail.com")))),
                () -> assertEquals(4, forgotPasswordTokenRepository.findAll().size())
        );
    }

    @Test
    @DisplayName("Successful get forgot password token")
    void getForgotPasswordTokenTest() {
        assertEquals(forgotPasswordTokenRepository.findByToken("51b1ec6c-2a57-4b42-b9f5-7efc5cc4a0f6").orElseThrow().getId(),
                forgotPasswordTokenService.getForgotPasswordToken("51b1ec6c-2a57-4b42-b9f5-7efc5cc4a0f6").getId());
    }

    @Test
    @DisplayName("Failed get forgot password token (throw NoSuchElementException)")
    void getForgotPasswordTokenThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> forgotPasswordTokenService.getForgotPasswordToken("token"));
    }

    @Test
    @DisplayName("Generate forgot password token")
    void generateForgotPasswordTokenTest() {
        assertNotNull(
                forgotPasswordTokenService.generateForgotPasswordToken(testUtils.getUserByEmail("sidor@gmail.com"))
                        .getToken()
        );
    }

    @Test
    @DisplayName("Successful validate forgot password token")
    void validateForgotPasswordTokenTest() {
        ForgotPasswordToken forgotPasswordToken = forgotPasswordTokenService
                .getForgotPasswordToken("8ac319b4-990f-466f-8a5a-7c2a028b430c");
        assertDoesNotThrow(() -> forgotPasswordTokenService
                .validateForgotPasswordToken(forgotPasswordToken));
    }

    @Test
    @DisplayName("Failed validate forgot password token (throw TokenAlreadyConfirmedException)")
    void validateForgotPasswordTokenThrowTokenAlreadyConfirmedExceptionTest() {
        ForgotPasswordToken forgotPasswordToken = forgotPasswordTokenService
                .getForgotPasswordToken("51b1ec6c-2a57-4b42-b9f5-7efc5cc4a0f6");
        assertThrows(TokenAlreadyConfirmedException.class,
                () -> forgotPasswordTokenService.validateForgotPasswordToken(forgotPasswordToken));
    }

    @Test
    @DisplayName("Failed validate forgot password token (throw TokenExpiredException)")
    void validateForgotPasswordTokenThrowTokenExpiredExceptionTest() {
        ForgotPasswordToken forgotPasswordToken = forgotPasswordTokenService
                .getForgotPasswordToken("8ac319b4-990f-466f-8a5a-expired");
        assertThrows(TokenExpiredException.class,
                () -> forgotPasswordTokenService.validateForgotPasswordToken(forgotPasswordToken));
    }
}