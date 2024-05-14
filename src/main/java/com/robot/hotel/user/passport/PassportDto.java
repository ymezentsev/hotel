package com.robot.hotel.user.passport;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "Dto for Passport")
public record PassportDto(Long id, String serialNumber, String country, LocalDate issueDate) {
}