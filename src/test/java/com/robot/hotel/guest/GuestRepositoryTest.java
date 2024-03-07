package com.robot.hotel.guest;

import com.robot.hotel.ContainerConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureDataJpa
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = ContainerConfiguration.class)
class GuestRepositoryTest {
/*    @Container
    @ServiceConnection
    static MySQLContainer<?> mySql = new MySQLContainer<>("mysql:8.0");*/

    @Autowired
    GuestRepository guestRepository;

    @Test
    @DisplayName("Check by email if guest exists")
    void existsByEmailTest() {
        assertAll(
                () -> assertTrue(guestRepository.existsByEmail("sidor@gmail.com")),
                () -> assertTrue(guestRepository.existsByEmail("dmitr@gmail.com")),
                () -> assertFalse(guestRepository.existsByEmail("dmitr123@gmail.com"))
        );
    }

    @Test
    @DisplayName("Check by tel.number if guest exists")
    void existsByTelNumberTest() {
        assertAll(
                () -> assertTrue(guestRepository.existsByTelNumber("0965467834")),
                () -> assertTrue(guestRepository.existsByTelNumber("0954375647")),
                () -> assertFalse(guestRepository.existsByTelNumber("+0934375647"))
        );
    }

    @Test
    @DisplayName("Check by passport serial number if guest exists")
    void existsByPassportSerialNumberTest() {
        assertAll(
                () -> assertTrue(guestRepository.existsByPassportSerialNumber("bb345678")),
                () -> assertTrue(guestRepository.existsByPassportSerialNumber("va123456")),
                () -> assertFalse(guestRepository.existsByPassportSerialNumber("+va123962"))
        );
    }

    @Test
    @DisplayName("Find guest by email")
    void findByEmailTest() {
        assertAll(
                () -> assertTrue(guestRepository.findByEmail("sidor@gmail.com").isPresent()),
                () -> assertTrue(guestRepository.findByEmail("dmitr@gmail.com").isPresent()),
                () -> assertTrue(guestRepository.findByEmail("dmitr123@gmail.com").isEmpty())
        );
    }

    @Test
    @DisplayName("Find guest by tel.number")
    void findByTelNumberTest() {
        assertAll(
                () -> assertTrue(guestRepository.findByTelNumber("0965467834").isPresent()),
                () -> assertTrue(guestRepository.findByTelNumber("0954375647").isPresent()),
                () -> assertTrue(guestRepository.findByTelNumber("+0934375647").isEmpty())
        );
   }

    @Test
    @DisplayName("Find guest by passport serial number")
    void findByPassportSerialNumberTest() {
        assertAll(
                () -> assertTrue(guestRepository.findByPassportSerialNumber("bb345678").isPresent()),
                () -> assertTrue(guestRepository.findByPassportSerialNumber("va123456").isPresent()),
                () -> assertTrue(guestRepository.findByPassportSerialNumber("+va123962").isEmpty())
        );
    }

    @Test
    @DisplayName("Find guest by lastname")
    void findByLastNameTest() {
        assertAll(
                () -> assertEquals(2, guestRepository.findByLastName("sidorov").size()),
                () -> assertEquals(1, guestRepository.findByLastName("dmitrenko").size()),
                () -> assertEquals(0, guestRepository.findByLastName("grigorenko").size())
        );
    }

    @Test
    @DisplayName("Find guest by reservation")
    void findByReservationsIdTest() {
        assertAll(
                () -> assertEquals(2, guestRepository.findByReservationsId(1L).size()),
                () -> assertEquals(0, guestRepository.findByReservationsId(10L).size())
        );
    }
}