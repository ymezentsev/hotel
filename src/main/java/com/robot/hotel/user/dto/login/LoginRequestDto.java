package com.robot.hotel.user.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto for login user")
public class LoginRequestDto {
    @NotBlank
    @Schema(description = "User email", example = "johndoe@gmail.com")
    private String email;

    @NotBlank
    @Schema(description = "User password", example = "Qwerty123456")
    private String password;
}
