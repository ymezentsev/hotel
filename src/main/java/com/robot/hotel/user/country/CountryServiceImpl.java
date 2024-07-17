package com.robot.hotel.user.country;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService{
    private final CountryRepository countryRepository;
    private static final String TEL_CODE_IS_NOT_EXISTS = "Such phone code is not exists";

    @Override
    public Country getCountryFromTelCode(String telCode) {
        List<Country> countries = countryRepository.findByPhoneCode(telCode);
        if (countries.isEmpty()) {
            throw new NoSuchElementException(TEL_CODE_IS_NOT_EXISTS);
        }
        return countries.get(0);
    }
}
