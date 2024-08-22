package com.robot.hotel.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dto for Role")
public record RoleDto(String name) {
}