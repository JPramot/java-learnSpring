package dev.lpa.goutbackend.tour;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.lpa.goutbackend.tour.dtos.CreateTourDto;
import dev.lpa.goutbackend.tour.models.Tour;

public interface TourService {

    Tour createTour(CreateTourDto tour);

    Tour gettourById(int id);

    Page<Tour> getPageTour(Pageable pageable);

} 
