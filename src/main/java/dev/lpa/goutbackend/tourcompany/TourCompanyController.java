package dev.lpa.goutbackend.tourcompany;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.lpa.goutbackend.tourcompany.dtos.RegisterTourCompanyDto;
import dev.lpa.goutbackend.tourcompany.models.TourCompany;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/tour-companies")
public class TourCompanyController {
    private final TourCompanyService tourCompanyService;

    public TourCompanyController(TourCompanyService tourCompanyService) {
        this.tourCompanyService = tourCompanyService;
    }

    @PostMapping
    public ResponseEntity<TourCompany> registerTourCompany(@RequestBody @Validated RegisterTourCompanyDto body) {
        //NOTE: create new tourCompany
        TourCompany tourCompany = tourCompanyService.registerTourCompany(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(tourCompany);
    }
    
    @PatchMapping("/{id}/approve")
    public ResponseEntity<TourCompany> approveTourCompany(@PathVariable Integer id) {
        
        TourCompany tourCompany = tourCompanyService.approveTourCompany(id);
        return ResponseEntity.ok(tourCompany);

    }
}
