package com.robot.hotel.user.repository;

import com.robot.hotel.ContainerConfiguration;
import com.robot.hotel.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureDataJpa
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @ParameterizedTest
    @DisplayName("Check by email if user exists")
    @CsvSource(value = {
            "sidor@gmail.com, true",
            "dmitr@gmail.com, true",
            "dmitr123@gmail.com, false"
    })
    void existsByEmailTest(String email, boolean result) {
        assertEquals(result, userRepository.existsByEmail(email));
    }

    @ParameterizedTest
    @DisplayName("Check by phone number if user exists")
    @CsvSource(value = {
            "965467834, true",
            "954375647, true",
            "+934375647, false"
    })
    void existsByPhoneNumberTest(String phoneNumber, boolean result) {
        assertEquals(result, userRepository.existsByPhoneNumber(phoneNumber));
    }

    @ParameterizedTest
    @DisplayName("Check by passport serial number if user exists")
    @CsvSource(value = {
            "bb345678, true",
            "va123456, true",
            "+va123962, false"
    })
    void existsByPassportSerialNumberTest(String passportSerialNumber, boolean result) {
        assertEquals(result, userRepository.existsByPassportSerialNumber(passportSerialNumber));
    }

    @ParameterizedTest
    @DisplayName("Find user by email")
    @CsvSource(value = {
            "sidor@gmail.com, true",
            "dmitr@gmail.com, true",
            "dmitr123@gmail.com, false"
    })
    void findByEmailTest(String email, boolean result) {
        assertEquals(result, userRepository.findByEmail(email).isPresent());
    }

    @ParameterizedTest
    @DisplayName("Find user by phone number")
    @CsvSource(value = {
            "965467834, true",
            "954375647, true",
            "+934375647, false"
    })
    void findByPhoneNumberTest(String phoneNumber, boolean result) {
        assertEquals(result, userRepository.findByPhoneNumber(phoneNumber).isPresent());
    }

    @ParameterizedTest
    @DisplayName("Find user by passport serial number")
    @CsvSource(value = {
            "bb345678, true",
            "va123456, true",
            "+va123962, false"
    })
    void findByPassportSerialNumberTest(String passportSerialNumber, boolean result) {
        assertEquals(result, userRepository.findByPassportSerialNumber(passportSerialNumber).isPresent());
    }
}