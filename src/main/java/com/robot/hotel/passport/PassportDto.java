package com.robot.hotel.passport;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto for Passport")
public class PassportDto {

    private Long id;

    private String serialNumber;

    private String country;

    private LocalDate issueDate;
}
