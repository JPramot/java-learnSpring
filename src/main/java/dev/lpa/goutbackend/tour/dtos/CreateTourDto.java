package dev.lpa.goutbackend.tour.dtos;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTourDto(
    @NotNull Integer tour_company_id, 
    @NotBlank String title, 
    int number_of_people,
    @NotBlank String description,
    @NotBlank String location,
    @NotNull Instant activity_date
) {

}
