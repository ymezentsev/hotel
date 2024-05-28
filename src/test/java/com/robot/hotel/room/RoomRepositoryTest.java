package com.robot.hotel.room;

import com.robot.hotel.ContainerConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureDataJpa
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)
class RoomRepositoryTest {
    @Autowired
    RoomRepository roomRepository;

    @Test
    @DisplayName("Check by number if room exists")
    void existsByNumberTest() {
        assertAll(
                () -> assertTrue(roomRepository.existsByNumber("101")),
                () -> assertTrue(roomRepository.existsByNumber("202")),
                () -> assertFalse(roomRepository.existsByNumber("105"))
        );
    }

    @Test
    @DisplayName("Find room by number")
    void findByNumberTest() {
        assertAll(
                () -> assertTrue(roomRepository.findByNumber("101").isPresent()),
                () -> assertTrue(roomRepository.findByNumber("202").isPresent()),
                () -> assertTrue(roomRepository.findByNumber("105").isEmpty())
        );
    }

    @Test
    @DisplayName("Find rooms by type")
    void findByRoomTypeIdTest() {
        assertAll(
                () -> assertEquals(1, roomRepository.findByRoomTypeId(1L, Pageable.unpaged())
                        .getTotalElements()),
                () -> assertEquals(2, roomRepository.findByRoomTypeId(2L, Pageable.unpaged())
                        .getTotalElements()),
                () -> assertEquals(0, roomRepository.findByRoomTypeId(4L, Pageable.unpaged())
                        .getTotalElements())
        );
    }

    @Test
    @DisplayName("Find rooms with price more or equal than given")
    void findByPriceMoreThanOrEqualTest() {
        assertAll(
                () -> assertEquals(1, roomRepository
                        .findByPriceMoreThanOrEqual(BigDecimal.valueOf(5000), Pageable.unpaged())
                        .getTotalElements()),
                () -> assertEquals(3, roomRepository
                        .findByPriceMoreThanOrEqual(BigDecimal.valueOf(1500), Pageable.unpaged())
                        .getTotalElements()),
                () -> assertEquals(0, roomRepository
                        .findByPriceMoreThanOrEqual(BigDecimal.valueOf(5500), Pageable.unpaged())
                        .getTotalElements())
        );
    }

    @Test
    @DisplayName("Find rooms with price less or equal than given")
    void findByPriceLessThanOrEqualTest() {
        assertAll(
                () -> assertEquals(4, roomRepository
                        .findByPriceLessThanOrEqual(BigDecimal.valueOf(1500), Pageable.unpaged())
                        .getTotalElements()),
                () -> assertEquals(2, roomRepository
                        .findByPriceLessThanOrEqual(BigDecimal.valueOf(1000), Pageable.unpaged())
                        .getTotalElements()),
                () -> assertEquals(0, roomRepository
                        .findByPriceLessThanOrEqual(BigDecimal.valueOf(500), Pageable.unpaged())
                        .getTotalElements())
        );
    }

    @Test
    @DisplayName("Find rooms with max count of guests more or equal than given")
    void findByGuestsCountTest() {
        assertAll(
                () -> assertEquals(0, roomRepository
                        .findByGuestsCount(5, Pageable.unpaged())
                        .getTotalElements()),
                () -> assertEquals(1, roomRepository
                        .findByGuestsCount(3, Pageable.unpaged())
                        .getTotalElements()),
                () -> assertEquals(5, roomRepository
                        .findByGuestsCount(2, Pageable.unpaged())
                        .getTotalElements())
        );
    }

    @Test
    @DisplayName("Find rooms without reservations")
    void findRoomsWithoutReservationsTest() {
        assertEquals(2, roomRepository.findRoomsWithoutReservations().size());
    }
}