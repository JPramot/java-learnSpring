package dev.lpa.goutbackend.tour.models;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Table;

import dev.lpa.goutbackend.tourcompany.models.TourCompany;

@Table("tour")
public record Tour(
    @Id Integer id, 
    AggregateReference<TourCompany, Integer> tour_company_id,
    String title, 
    int number_of_people,
    String description,
    String location,
    Instant activity_date,
    String status) {

}

