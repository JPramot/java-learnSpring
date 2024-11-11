package dev.lpa.goutbackend.user.dtos;

public record UpdateUserDto(
    String firstName,
    String lastName,
    String phoneNumber
) {

}
