package com.robot.hotel.country;

import com.robot.hotel.ContainerConfiguration;
import com.robot.hotel.country.CountryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureDataJpa
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)
class CountryRepositoryTest {
    @Autowired
    CountryRepository countryRepository;

    @Test
    void findByPhoneCodeTest() {
        assertEquals("UKR", countryRepository.findByPhoneCode("+380").get(0).getCountryCode());
    }
}