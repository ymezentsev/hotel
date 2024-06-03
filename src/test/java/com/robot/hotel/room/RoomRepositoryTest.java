package com.robot.hotel.room;

import com.robot.hotel.ContainerConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    @ParameterizedTest
    @DisplayName("Find rooms by type")
    @CsvSource(value = {
            "1, 1",
            "2, 2",
            "4, 0"
    })
    void findByRoomTypeIdTest(Long id, int result) {
        assertEquals(result, roomRepository.findByRoomTypeId(id, Pageable.unpaged()).getTotalElements());
    }

    @ParameterizedTest
    @DisplayName("Find rooms with price more or equal than given")
    @CsvSource(value = {
            "5000, 1",
            "1500, 3",
            "5500, 0"
    })
    void findByPriceMoreThanOrEqualTest(double price, int result) {
        assertEquals(result, roomRepository
                .findByPriceMoreThanOrEqual(BigDecimal.valueOf(price), Pageable.unpaged())
                .getTotalElements());
    }

    @ParameterizedTest
    @DisplayName("Find rooms with price less or equal than given")
    @CsvSource(value = {
            "1500, 4",
            "1000, 2",
            "500, 0"
    })
    void findByPriceLessThanOrEqualTest(double price, int result) {
        assertEquals(result, roomRepository
                .findByPriceLessThanOrEqual(BigDecimal.valueOf(price), Pageable.unpaged())
                .getTotalElements());
    }

    @ParameterizedTest
    @DisplayName("Find rooms with max count of guests more or equal than given")
    @CsvSource(value = {
            "5, 0",
            "3, 1",
            "2, 5"
    })
    void findByGuestsCountTest(int guestsCount, int result) {
        assertEquals(result, roomRepository
                .findByGuestsCount(guestsCount, Pageable.unpaged())
                .getTotalElements());
    }

    @Test
    @DisplayName("Find rooms without reservations")
    void findRoomsWithoutReservationsTest() {
        assertEquals(2, roomRepository.findRoomsWithoutReservations().size());
    }
}