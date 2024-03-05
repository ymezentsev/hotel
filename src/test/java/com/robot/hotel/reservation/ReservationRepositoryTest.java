package com.robot.hotel.reservation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class ReservationRepositoryTest {
    @Container
    @ServiceConnection
    static MySQLContainer<?> mySql = new MySQLContainer<>("mysql:8.0");

    @Autowired
    ReservationRepository reservationRepository;

    @Test
    @DisplayName("Find reservations by user id")
    void findByUserIdTest() {
        assertAll(
                () -> assertEquals(2, reservationRepository.findByUsersId(1L).size()),
                () -> assertEquals(3, reservationRepository.findByUsersId(2L).size()),
                () -> assertEquals(0, reservationRepository.findByUsersId(3L).size())
        );
    }

    @Test
    @DisplayName("Find reservations by room id")
    void findByRoomIdTest() {
        assertAll(
                () -> assertEquals(1, reservationRepository.findByRoomId(1L).size()),
                () -> assertEquals(2, reservationRepository.findByRoomId(5L).size()),
                () -> assertEquals(0, reservationRepository.findByRoomId(3L).size())
        );
    }

    @Test
    @DisplayName("Find free rooms for which reservations exist")
    void findFreeRoomsWithReservationsTest() {
        assertAll(
                () -> assertEquals(1, reservationRepository
                        .findFreeRoomsWithReservations(LocalDate.now(), LocalDate.now().plusDays(1)).size()),
                () -> assertEquals(2, reservationRepository
                        .findFreeRoomsWithReservations(LocalDate.now().plusDays(3), LocalDate.now().plusDays(4)).size()),
                () -> assertEquals(3, reservationRepository
                        .findFreeRoomsWithReservations(LocalDate.now().plusDays(6), LocalDate.now().plusDays(7)).size())
        );
    }

    @Test
    @DisplayName("Find current reservations")
    void findCurrentReservationsTest() {
        assertEquals(3, reservationRepository.findCurrentReservations().size());
    }

    @Test
    @DisplayName("Find current reservations for specific room")
    void findCurrentReservationsForRoomTest() {
        assertAll(
                () -> assertEquals(1, reservationRepository
                        .findCurrentReservationsForRoom(5L).size()),
                () -> assertEquals(1, reservationRepository
                        .findCurrentReservationsForRoom(4L).size()),
                () -> assertEquals(0, reservationRepository
                        .findCurrentReservationsForRoom(2L).size())
        );
    }
}