package com.robot.hotel.passport;

import org.springframework.stereotype.Service;

@Service
public class PassportMapper {
    public PassportDto buildPassportDto(Passport passport) {
        if(passport == null){
            return null;
        }

        return PassportDto.builder()
                .id(passport.getId())
                .serialNumber(passport.getSerialNumber())
                .country(passport.getCountry().getName())
                .issueDate(passport.getIssueDate())
                .build();
    }
}
