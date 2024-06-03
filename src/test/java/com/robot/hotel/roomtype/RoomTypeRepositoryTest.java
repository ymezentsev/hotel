package com.robot.hotel.roomtype;

import com.robot.hotel.ContainerConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureDataJpa
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)
class RoomTypeRepositoryTest {
    @Autowired
    RoomTypeRepository roomTypeRepository;

    @ParameterizedTest
    @DisplayName("Find room type by type")
    @CsvSource(value = {
            "lux, true",
            "new lux, false"
    })
    void findByTypeTest(String roomType, boolean result) {
        assertEquals(result, roomTypeRepository.findByType(roomType).isPresent());
    }

    @ParameterizedTest
    @DisplayName("Check if room type exists")
    @CsvSource(value = {
            "lux, true",
            "new lux, false"
    })
    void existsByTypeTest(String roomType, boolean result) {
        assertEquals(result, roomTypeRepository.existsByType(roomType));
    }
}