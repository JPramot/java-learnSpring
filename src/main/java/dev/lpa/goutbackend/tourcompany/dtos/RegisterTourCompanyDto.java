package dev.lpa.goutbackend.tourcompany.dtos;

import jakarta.validation.constraints.NotBlank;

public record RegisterTourCompanyDto(
    Integer id, 
    @NotBlank String name, 
    @NotBlank String userName,
    @NotBlank String password,
    String status) {

}
