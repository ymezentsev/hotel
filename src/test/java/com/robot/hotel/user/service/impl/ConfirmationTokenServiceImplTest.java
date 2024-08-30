package com.robot.hotel.user.service.impl;

import com.robot.hotel.ContainerConfiguration;
import com.robot.hotel.DBInitializer;
import com.robot.hotel.DBUtils;
import com.robot.hotel.exception.TokenAlreadyConfirmedException;
import com.robot.hotel.exception.TokenExpiredException;
import com.robot.hotel.user.model.ConfirmationToken;
import com.robot.hotel.user.repository.ConfirmationTokenRepository;
import com.robot.hotel.user.service.ConfirmationTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)
class ConfirmationTokenServiceImplTest {
    @Autowired
    ConfirmationTokenService confirmationTokenService;

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    DBInitializer dbInitializer;

    @Autowired
    DBUtils testUtils;

    @BeforeEach
    void setUp() {
        dbInitializer.populateDB();
    }

    @Test
    @DisplayName("Save confirmation token")
    void saveConfirmationTokenTest() {
        assertAll(
                () -> assertNotNull(
                        confirmationTokenService.saveConfirmationToken(
                                confirmationTokenService.generateConfirmationToken(
                                        testUtils.getUserByEmail("sidor@gmail.com")))),
                () -> assertEquals(5, confirmationTokenRepository.findAll().size())
        );
    }

    @Test
    @DisplayName("Successful get confirmation token")
    void getConfirmationTokenTest() {
        assertEquals(confirmationTokenRepository.findByToken("ec410724-03b8-427a-a579-cbe965a543c7").orElseThrow().getId(),
                confirmationTokenService.getConfirmationToken("ec410724-03b8-427a-a579-cbe965a543c7").getId());
    }

    @Test
    @DisplayName("Failed get confirmation token (throw NoSuchElementException)")
    void getConfirmationTokenThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> confirmationTokenService.getConfirmationToken("token"));
    }

    @Test
    @DisplayName("Generate confirmation token")
    void generateConfirmationTokenTest() {
        assertNotNull(
                confirmationTokenService.generateConfirmationToken(testUtils.getUserByEmail("sidor@gmail.com"))
                        .getToken()
        );
    }

    @Test
    @DisplayName("Successful validate confirmation token")
    void validateConfirmationTokenTest() {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getConfirmationToken("6453fbfb-8ff9-4dea-b8c9-notConfirmed");
        assertDoesNotThrow(() -> confirmationTokenService
                .validateConfirmationToken(confirmationToken));
    }

    @Test
    @DisplayName("Failed validate confirmation token (throw TokenAlreadyConfirmedException)")
    void validateConfirmationTokenThrowTokenAlreadyConfirmedExceptionTest() {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getConfirmationToken("6453fbfb-8ff9-4dea-b8c9-3c6807410cdb");
        assertThrows(TokenAlreadyConfirmedException.class,
                () -> confirmationTokenService.validateConfirmationToken(confirmationToken));
    }

    @Test
    @DisplayName("Failed validate confirmation token (throw TokenExpiredException)")
    void validateConfirmationTokenThrowTokenExpiredExceptionTest() {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getConfirmationToken("6453fbfb-8ff9-4dea-b8c9-expired");
        assertThrows(TokenExpiredException.class,
                () -> confirmationTokenService.validateConfirmationToken(confirmationToken));
    }
}