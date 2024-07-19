package com.robot.hotel.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Dto for Passport")
public record PassportDto(Long id, String serialNumber, String country, LocalDate issueDate) {
}