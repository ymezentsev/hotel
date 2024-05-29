package com.robot.hotel.user;

import com.robot.hotel.ContainerConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

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
    @DisplayName("Check by phone number if user exists")
    void existsByPhoneNumberTest() {
        assertAll(
                () -> assertTrue(userRepository.existsByPhoneNumber("965467834")),
                () -> assertTrue(userRepository.existsByPhoneNumber("954375647")),
                () -> assertFalse(userRepository.existsByPhoneNumber("+934375647"))
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
    @DisplayName("Find user by phone number")
    void findByPhoneNumberTest() {
        assertAll(
                () -> assertTrue(userRepository.findByPhoneNumber("965467834").isPresent()),
                () -> assertTrue(userRepository.findByPhoneNumber("954375647").isPresent()),
                () -> assertTrue(userRepository.findByPhoneNumber("+934375647").isEmpty())
        );
    }

    @Test
    @DisplayName("Find user by full phone number")
    void findByPhoneFullNumberTest() {
        assertAll(
                () -> assertTrue(userRepository.findByFullPhoneNumber("+380965467834").isPresent()),
                () -> assertTrue(userRepository.findByFullPhoneNumber("+390934560912").isPresent()),
                () -> assertTrue(userRepository.findByFullPhoneNumber("+10934375647").isEmpty())
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
                () -> assertEquals(2, userRepository
                        .findByLastName("sidorov", Pageable.unpaged()).getTotalElements()),
                () -> assertEquals(1, userRepository
                        .findByLastName("dmitrenko", Pageable.unpaged()).getTotalElements()),
                () -> assertEquals(0, userRepository
                        .findByLastName("grigorenko", Pageable.unpaged()).getTotalElements())
        );
    }

    @Test
    @DisplayName("Find user by reservation")
    void findByReservationsIdTest() {
        assertAll(
                () -> assertEquals(2, userRepository
                        .findByReservationsId(1L, Pageable.unpaged()).getTotalElements()),
                () -> assertEquals(0, userRepository
                        .findByReservationsId(100L, Pageable.unpaged()).getTotalElements())
        );
    }

    @Test
    @DisplayName("Find users by role")
    void findByRoleTest() {
        assertAll(
                () -> assertEquals(4, userRepository
                        .findByRole(Role.USER, Pageable.unpaged()).getTotalElements()),
                () -> assertEquals(1, userRepository
                        .findByRole(Role.MANAGER, Pageable.unpaged()).getTotalElements()),
                () -> assertEquals(1, userRepository
                        .findByRole(Role.ADMIN, Pageable.unpaged()).getTotalElements())
        );
    }
}