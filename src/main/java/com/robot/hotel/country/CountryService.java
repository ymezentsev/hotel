package com.robot.hotel.country;

import java.util.List;

public interface CountryService {
    List<CountryDto> findByTelCode(String telCode);
}
