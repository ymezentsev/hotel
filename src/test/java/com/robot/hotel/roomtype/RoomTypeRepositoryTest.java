package com.robot.hotel.roomtype;

import com.robot.hotel.ContainerConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@AutoConfigureDataJpa
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = ContainerConfiguration.class)
class RoomTypeRepositoryTest {
    @Autowired
    RoomTypeRepository roomTypeRepository;

    @Test
    @DisplayName("Find room type by type")
    void findByTypeTest() {
        assertAll(
                () -> assertTrue(roomTypeRepository.findByType("lux").isPresent()),
                () -> assertFalse(roomTypeRepository.findByType("new lux").isPresent())
        );
    }

    @Test
    @DisplayName("Check if room type exists")
    void existsByTypeTest() {
        assertAll(
                () -> assertTrue(roomTypeRepository.existsByType("lux")),
                () -> assertFalse(roomTypeRepository.existsByType("new lux"))
        );
    }
}