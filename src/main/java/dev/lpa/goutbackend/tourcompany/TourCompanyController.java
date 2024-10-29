package dev.lpa.goutbackend.tourcompany;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(TourCompanyController.class);
    private final TourCompanyService tourCompanyService;

    public TourCompanyController(TourCompanyService tourCompanyService) {
        this.tourCompanyService = tourCompanyService;
    }

    @PostMapping
    public ResponseEntity<TourCompany> registerTourCompany(@RequestBody @Validated RegisterTourCompanyDto body) {
        //NOTE: create new tourCompany
        TourCompany tourCompany = tourCompanyService.registerTourCompany(body);

        //NOTE: create tourcCompany login credential
        return ResponseEntity.status(HttpStatus.CREATED).body(tourCompany);
    }
    
    @PatchMapping("/{id}/approve")
    public ResponseEntity<TourCompany> patchMethodName(@PathVariable Integer id) {
        
        TourCompany tourCompany = tourCompanyService.approveTourCompany(id);
        if(tourCompany == null) {
            logger.info("[approveCompany] tourCompany id: {} not found", id);
            return ResponseEntity.notFound().build();
        }
        logger.info("[approveCompany] tourCompany id: {} was approves", id);
        return ResponseEntity.ok(tourCompany);

    }
}
