package com.robot.hotel.country;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService{
    private final CountryRepository countryRepository;
    private static final String PHONE_CODE_IS_NOT_EXISTS = "Such phone code is not exists";

    @Override
    public Country getCountryFromPhoneCode(String phoneCode) {
        List<Country> countries = countryRepository.findByPhoneCode(phoneCode);
        if (countries.isEmpty()) {
            throw new NoSuchElementException(PHONE_CODE_IS_NOT_EXISTS);
        }
        return countries.get(0);
    }
}
