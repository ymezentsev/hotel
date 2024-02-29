package com.robot.hotel.reservation;

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
    void findByGuestsIdTest() {
        assertAll(
                () -> assertEquals(2, reservationRepository.findByGuestsId(1L).size()),
                () -> assertEquals(3, reservationRepository.findByGuestsId(2L).size()),
                () -> assertEquals(0, reservationRepository.findByGuestsId(3L).size())
        );
    }

    @Test
    void findByRoomIdTest() {
        assertAll(
                () -> assertEquals(1, reservationRepository.findByRoomId(1L).size()),
                () -> assertEquals(2, reservationRepository.findByRoomId(5L).size()),
                () -> assertEquals(0, reservationRepository.findByRoomId(3L).size())
        );
    }

    @Test
    void findFreeRoomsTest() {
        assertAll(
                () -> assertEquals(1, reservationRepository
                        .findFreeRooms(LocalDate.now(), LocalDate.now().plusDays(1)).size()),
                () -> assertEquals(2, reservationRepository
                        .findFreeRooms(LocalDate.now().plusDays(3), LocalDate.now().plusDays(4)).size()),
                () -> assertEquals(3, reservationRepository
                        .findFreeRooms(LocalDate.now().plusDays(6), LocalDate.now().plusDays(7)).size())
        );
    }

    @Test
    void findCurrentReservationsTest() {
        assertEquals(3, reservationRepository.findCurrentReservations().size());
    }

    @Test
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