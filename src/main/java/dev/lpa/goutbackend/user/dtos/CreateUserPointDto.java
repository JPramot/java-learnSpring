package dev.lpa.goutbackend.user.dtos;

import java.time.Instant;

import jakarta.validation.constraints.NotNull;

public record CreateUserPointDto(
    @NotNull int userId,
    @NotNull Instant lastUpdated
) {

}
