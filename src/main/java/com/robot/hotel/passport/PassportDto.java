package com.robot.hotel.passport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassportDto {

    private Long id;

    private String serialNumber;

    private String country;

    private LocalDate issueDate;
}
