package com.robot.hotel.search_criteria.room;

import com.robot.hotel.ContainerConfiguration;
import com.robot.hotel.room.Room;
import com.robot.hotel.room.dto.RoomSearchParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)
class RoomSpecificationBuilderTest {
    @Autowired
    RoomSpecificationBuilder roomSpecificationBuilder;

    @Test
    @DisplayName("Successful build specification")
    void buildTest() {
        Specification<Room> originalSpec = Specification.where(null);

        RoomSearchParameters searchParameters = new RoomSearchParameters(
                new String[]{"lux"},
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(1000),
                3);
        assertAll(
                () -> assertEquals(originalSpec, roomSpecificationBuilder.build((RoomSearchParameters) null)),
                () -> assertNotEquals(originalSpec, roomSpecificationBuilder.build(searchParameters))
        );
    }
}