package com.robot.hotel.user.repository;

import com.robot.hotel.ContainerConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureDataJpa
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)
class ConfirmationTokenRepositoryTest {
    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    @ParameterizedTest
    @DisplayName("Find confirmation token by token")
    @CsvSource(value = {
            "ec410724-03b8-427a-a579-cbe965a543c7, true",
            "6453fbfb-8ff9-4dea-b8c9-expired, true",
            "token, false"
    })
    void findByTokenTest(String token, boolean result) {
        assertEquals(result, confirmationTokenRepository.findByToken(token).isPresent());
    }
}