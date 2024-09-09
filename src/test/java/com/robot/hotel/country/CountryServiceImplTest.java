package com.robot.hotel.country;

import com.robot.hotel.ContainerConfiguration;
import com.robot.hotel.exception.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)

class CountryServiceImplTest {
    @Autowired
    CountryService countryService;

    @ParameterizedTest
    @DisplayName("Successful find country")
    @CsvSource(value = {
            "+380, UKR",
            "+374, ARM",
            "+33, FRA",
            "+81, JPN"
    })
    void getCountryFromTelCodeTest(String phoneCode, String result) {
        assertEquals(result,
                countryService.getCountryFromPhoneCode(phoneCode).getCountryCode());
    }

    @Test
    @DisplayName("Fail find country")
    void getCountryFromTelCodeThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                ()->countryService.getCountryFromPhoneCode("+9999"));
    }
}