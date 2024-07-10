package com.robot.hotel.search_criteria.room;

import com.robot.hotel.ContainerConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)
class RoomSpecificationProviderManagerTest {
    @Autowired
    RoomSpecificationProviderManager roomSpecificationProviderManager;

    @ParameterizedTest
    @DisplayName("Successful get specification provider")
    @CsvSource(value = {
            "type, com.robot.hotel.search_criteria.room.TypeSpecificationProvider",
            "minPrice, com.robot.hotel.search_criteria.room.MinPriceSpecificationProvider",
            "maxPrice, com.robot.hotel.search_criteria.room.MaxPriceSpecificationProvider",
            "guestsCount, com.robot.hotel.search_criteria.room.GuestCountSpecificationProvider",
    })
    void getSpecificationProviderTest(String key, String className) throws ClassNotFoundException {
        assertInstanceOf(Class.forName(className),
                roomSpecificationProviderManager.getSpecificationProvider(key));
    }

    @Test
    @DisplayName("Fail get specification provider")
    void getSpecificationProviderTestThrowException() {
        assertThrows(RuntimeException.class,
                () -> roomSpecificationProviderManager.getSpecificationProvider("wrongKey"));
    }
}