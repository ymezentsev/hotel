package com.robot.hotel.user.passport;

import com.robot.hotel.ContainerConfiguration;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureDataJpa
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)
class PassportRepositoryTest {
    @Autowired
    PassportRepository passportRepository;

    @ParameterizedTest
    @CsvSource(value = {
            "bb345678, true",
            "va123456, true",
            "ba111111, false"
    })
    void existsBySerialNumberTest(String PassportSerialNumber, boolean result) {
        assertEquals(result, passportRepository.existsBySerialNumber(PassportSerialNumber));
   }
}