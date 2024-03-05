package com.robot.hotel.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class UserRepositoryTest {
    @Container
    @ServiceConnection
    static MySQLContainer<?> mySql = new MySQLContainer<>("mysql:8.0");

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("Check by email if user exists")
    void existsByEmailTest() {
        assertAll(
                () -> assertTrue(userRepository.existsByEmail("sidor@gmail.com")),
                () -> assertTrue(userRepository.existsByEmail("dmitr@gmail.com")),
                () -> assertFalse(userRepository.existsByEmail("dmitr123@gmail.com"))
        );
    }

    @Test
    @DisplayName("Check by tel.number if user exists")
    void existsByTelNumberTest() {
        assertAll(
                () -> assertTrue(userRepository.existsByTelNumber("0965467834")),
                () -> assertTrue(userRepository.existsByTelNumber("0954375647")),
                () -> assertFalse(userRepository.existsByTelNumber("+0934375647"))
        );
    }

    @Test
    @DisplayName("Check by passport serial number if user exists")
    void existsByPassportSerialNumberTest() {
        assertAll(
                () -> assertTrue(userRepository.existsByPassportSerialNumber("bb345678")),
                () -> assertTrue(userRepository.existsByPassportSerialNumber("va123456")),
                () -> assertFalse(userRepository.existsByPassportSerialNumber("+va123962"))
        );
    }

    @Test
    @DisplayName("Find user by email")
    void findByEmailTest() {
        assertAll(
                () -> assertTrue(userRepository.findByEmail("sidor@gmail.com").isPresent()),
                () -> assertTrue(userRepository.findByEmail("dmitr@gmail.com").isPresent()),
                () -> assertTrue(userRepository.findByEmail("dmitr123@gmail.com").isEmpty())
        );
    }

    @Test
    @DisplayName("Find user by tel.number")
    void findByTelNumberTest() {
        assertAll(
                () -> assertTrue(userRepository.findByTelNumber("0965467834").isPresent()),
                () -> assertTrue(userRepository.findByTelNumber("0954375647").isPresent()),
                () -> assertTrue(userRepository.findByTelNumber("+0934375647").isEmpty())
        );
   }

    @Test
    @DisplayName("Find user by passport serial number")
    void findByPassportSerialNumberTest() {
        assertAll(
                () -> assertTrue(userRepository.findByPassportSerialNumber("bb345678").isPresent()),
                () -> assertTrue(userRepository.findByPassportSerialNumber("va123456").isPresent()),
                () -> assertTrue(userRepository.findByPassportSerialNumber("+va123962").isEmpty())
        );
    }

    @Test
    @DisplayName("Find user by lastname")
    void findByLastNameTest() {
        assertAll(
                () -> assertEquals(2, userRepository.findByLastName("sidorov").size()),
                () -> assertEquals(1, userRepository.findByLastName("dmitrenko").size()),
                () -> assertEquals(0, userRepository.findByLastName("grigorenko").size())
        );
    }

    @Test
    @DisplayName("Find user by reservation")
    void findByReservationsIdTest() {
        assertAll(
                () -> assertEquals(2, userRepository.findByReservationsId(1L).size()),
                () -> assertEquals(0, userRepository.findByReservationsId(10L).size())
        );
    }
}