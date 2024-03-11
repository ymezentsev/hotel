package com.robot.hotel.country;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    @Override
    public List<CountryDto> findByTelCode(String telCode) {
        return countryRepository.findByTelCode(telCode).stream()
                .map(countryMapper::buildCountryDto)
                .toList();
    }
}
