package com.robot.hotel.country;

import org.springframework.stereotype.Service;

@Service
public class CountryMapper {
    public CountryDto buildCountryDto(Country country) {
        return CountryDto.builder()
                .code(country.getCode())
                .name(country.getName())
                .telCode(country.getTelCode())
                .build();
    }
}
