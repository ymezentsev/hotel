package com.robot.hotel.reservation;

import com.robot.hotel.ContainerConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureDataJpa
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)
class ReservationRepositoryTest {
    @Autowired
    ReservationRepository reservationRepository;

    @ParameterizedTest
    @DisplayName("Find reservations by user id")
    @CsvSource(value = {
            "2, 2",
            "3, 3",
            "4, 0"
    })
    void findByUserIdTest(Long userId, int result) {
        assertEquals(result,
                reservationRepository.findByUsersId(userId, Pageable.unpaged()).getTotalElements());
    }

    @ParameterizedTest
    @DisplayName("Find reservations by room id")
    @CsvSource(value = {
            "1, 1",
            "5, 2",
            "3, 0"
    })
    void findByRoomIdTest(Long roomId, int result) {
        assertEquals(result,
                reservationRepository.findByRoomId(roomId, Pageable.unpaged()).getTotalElements());
    }

    @Test
    @DisplayName("Find current reservations")
    void findCurrentReservationsTest() {
        assertEquals(3,
                reservationRepository.findCurrentReservations(Pageable.unpaged()).getTotalElements());
    }

    @ParameterizedTest
    @DisplayName("Find current reservations for specific room")
    @CsvSource(value = {
            "5, 1",
            "4, 1",
            "2, 0"
    })
    void findCurrentReservationsForRoomTest(Long roomId, int result) {
        assertEquals(result, reservationRepository
                .findCurrentReservationsForRoom(roomId, Pageable.unpaged()).getTotalElements());
    }
}