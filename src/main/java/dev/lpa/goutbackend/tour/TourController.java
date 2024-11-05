package dev.lpa.goutbackend.tour;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.lpa.goutbackend.tour.dtos.CreateTourDto;
import dev.lpa.goutbackend.tour.models.Tour;


@RestController
@RequestMapping("/api/v1/tours")
public class TourController {

    private final TourService tourService;

    public TourController(TourService tourService){
        this.tourService = tourService;
    }

    @PostMapping
    public ResponseEntity<Tour> createTour(@RequestBody @Validated CreateTourDto body) {
        Tour tour = tourService.createTour(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(tour);
    }

    @GetMapping
    //NOTE: pagination in Spring Boot
    public ResponseEntity<Page<Tour>> getPageTour(
            @RequestParam(required = true) int page, // page: 1,2,3
            @RequestParam(required = true) int size, // size: 10 -> [1-10], [11-20], [21-30]
            @RequestParam(required = false) String sortField, // sortField: title, activity_date
            @RequestParam(required = false) String sortDirection // sortDirection: asc, desc
    ) {

        Sort sort = Sort.by(Sort.Direction.valueOf(sortDirection.toUpperCase()), sortField);
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        
        return ResponseEntity.ok().body(tourService.getPageTour(pageable));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Tour> getTourById(@PathVariable int id) {
        return ResponseEntity.ok().body(tourService.gettourById(id));
    }

}
