package com.robot.hotel.room;

import com.robot.hotel.ContainerConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureDataJpa
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)
class RoomRepositoryTest {
    @Autowired
    RoomRepository roomRepository;

    @ParameterizedTest
    @DisplayName("Check by number if room exists")
    @CsvSource(value = {
            "101, true",
            "202, true",
            "105, false"
    })
    void existsByNumberTest(String number, boolean result) {
        assertEquals(result, roomRepository.existsByNumber(number));
    }

    @ParameterizedTest
    @DisplayName("Find room by number")
    @CsvSource(value = {
            "101, true",
            "202, true",
            "105, false"
    })
    void findByNumberTest(String number, boolean result) {
        assertEquals(result, roomRepository.findByNumber(number).isPresent());
    }

    @Test
    @DisplayName("Find free rooms")
    void findFreeRoomsTest() {
        assertAll(
                () -> assertEquals(3, roomRepository
                        .findFreeRooms(LocalDate.now(), LocalDate.now().plusDays(1)).size()),
                () -> assertEquals(4, roomRepository
                        .findFreeRooms(LocalDate.now().plusDays(3),
                                LocalDate.now().plusDays(4)).size()),
                () -> assertEquals(5, roomRepository
                        .findFreeRooms(LocalDate.now().plusDays(6),
                                LocalDate.now().plusDays(7)).size())
        );
    }
}