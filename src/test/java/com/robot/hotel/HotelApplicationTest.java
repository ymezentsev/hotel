package com.robot.hotel;

import com.robot.hotel.roomtype.RoomTypeController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)
class HotelApplicationTest {
    @Autowired
    RoomTypeController controller;

    @Test
    @DisplayName("Load app's context")
    void contextLoads() {
        assertThat(controller).isNotNull();
    }
}