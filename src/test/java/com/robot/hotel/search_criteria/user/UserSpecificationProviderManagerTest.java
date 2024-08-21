package com.robot.hotel.search_criteria.user;

import com.robot.hotel.ContainerConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)
class UserSpecificationProviderManagerTest {
    @Autowired
    UserSpecificationProviderManager userSpecificationProviderManager;

    @ParameterizedTest
    @DisplayName("Successful get specification provider")
    @CsvSource(value = {
            "firstname, com.robot.hotel.search_criteria.user.specification_providers.FirstnameSpecificationProvider",
            "lastname, com.robot.hotel.search_criteria.user.specification_providers.LastnameSpecificationProvider",
            "phoneNumber, com.robot.hotel.search_criteria.user.specification_providers.PhoneNumberSpecificationProvider",
            "email, com.robot.hotel.search_criteria.user.specification_providers.EmailSpecificationProvider",
            "role, com.robot.hotel.search_criteria.user.specification_providers.RoleSpecificationProvider",
            "passportSerialNumber, com.robot.hotel.search_criteria.user.specification_providers.PassportSerialNumberSpecificationProvider",
            "reservation, com.robot.hotel.search_criteria.user.specification_providers.ReservationSpecificationProvider",
            "countryCode, com.robot.hotel.search_criteria.user.specification_providers.CountryCodeSpecificationProvider",
    })
    void getSpecificationProviderTest(String key, String className) throws ClassNotFoundException {
        assertInstanceOf(Class.forName(className),
                userSpecificationProviderManager.getSpecificationProvider(key));
    }

    @Test
    @DisplayName("Fail get specification provider")
    void getSpecificationProviderTestThrowException() {
        assertThrows(RuntimeException.class,
                () -> userSpecificationProviderManager.getSpecificationProvider("wrongKey"));
    }
}