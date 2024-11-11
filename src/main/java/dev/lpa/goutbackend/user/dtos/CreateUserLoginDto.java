package dev.lpa.goutbackend.user.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserLoginDto(
    @NotNull Integer userId,
    @NotBlank String email,
    @NotBlank String password
) {

}
