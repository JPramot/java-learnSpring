package dev.lpa.goutbackend.tour.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;

import dev.lpa.goutbackend.tour.models.Tour;

public interface TourRepository extends ListCrudRepository<Tour, Integer> {

    Page<Tour> findAll(Pageable pageable);

}
