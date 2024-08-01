package com.robot.hotel.user.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
    @NotBlank
    @Schema(description = "User Email", example = "johndoe@gmail.com")
    private String email;

    @NotBlank
    @Schema(description = "User Password", example = "Qwerty123456")
    private String password;
}
