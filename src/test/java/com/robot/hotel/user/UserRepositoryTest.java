package com.robot.hotel.user;

import com.robot.hotel.ContainerConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureDataJpa
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)
class UserRepositoryTest {
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
                () -> assertTrue(userRepository.existsByTelNumber("965467834")),
                () -> assertTrue(userRepository.existsByTelNumber("954375647")),
                () -> assertFalse(userRepository.existsByTelNumber("+934375647"))
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
                () -> assertTrue(userRepository.findByTelNumber("965467834").isPresent()),
                () -> assertTrue(userRepository.findByTelNumber("954375647").isPresent()),
                () -> assertTrue(userRepository.findByTelNumber("+934375647").isEmpty())
        );
    }

    @Test
    @DisplayName("Find user by full tel.number")
    void findByTelFullNumberTest() {
        assertAll(
                () -> assertTrue(userRepository.findByFullTelNumber("+380965467834").isPresent()),
                () -> assertTrue(userRepository.findByFullTelNumber("+390934560912").isPresent()),
                () -> assertTrue(userRepository.findByFullTelNumber("+10934375647").isEmpty())
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
                () -> assertEquals(0, userRepository.findByReservationsId(100L).size())
        );
    }

    @Test
    @DisplayName("Find users by role")
    void findByRoleTest() {
        assertAll(
                () -> assertEquals(4, userRepository.findByRole(Role.USER).size()),
                () -> assertEquals(1, userRepository.findByRole(Role.MANAGER).size()),
                () -> assertEquals(1, userRepository.findByRole(Role.ADMIN).size())
        );
    }
}