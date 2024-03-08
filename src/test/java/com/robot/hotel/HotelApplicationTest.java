package com.robot.hotel;

import com.robot.hotel.roomtype.RoomTypeController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = ContainerConfiguration.class)
class HotelApplicationTest {
    @Autowired
    private RoomTypeController controller;

    @Test
    @DisplayName("Load app's context")
    void contextLoads() {
        assertThat(controller).isNotNull();
    }
}