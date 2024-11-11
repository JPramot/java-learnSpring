package dev.lpa.goutbackend.user.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateUserDto(
    @NotBlank String firstName,
    @NotBlank String lastName,
    @NotBlank String phoneNumber,
    @NotBlank String email,
    @NotBlank String password    
) {

}
