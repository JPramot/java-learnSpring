package dev.lpa.goutbackend.tour;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.time.Instant;
import java.time.Duration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.core.mapping.AggregateReference;


import dev.lpa.goutbackend.commons.enumulation.TourCompanyStatus;
import dev.lpa.goutbackend.commons.enumulation.TourStatus;
import dev.lpa.goutbackend.commons.exceptions.EntityNotFound;
import dev.lpa.goutbackend.tour.dtos.CreateTourDto;
import dev.lpa.goutbackend.tour.models.Tour;
import dev.lpa.goutbackend.tour.models.TourCount;
import dev.lpa.goutbackend.tour.repo.TourCountRepository;
import dev.lpa.goutbackend.tour.repo.TourRepository;
import dev.lpa.goutbackend.tourcompany.models.TourCompany;
import dev.lpa.goutbackend.tourcompany.repo.TourCompanyRepository;

@ExtendWith(MockitoExtension.class)
class TourServiceTest {

    @InjectMocks
    private TourServiceImp tourServiceImp;

    @Mock
    private TourRepository tourRepository;

    @Mock 
    private TourCountRepository tourCountRepository;

    @Mock
    private TourCompanyRepository tourCompanyRepository;

    @Test
    void shouldCreateTourWhenSuccess() {
        Instant activityDate = Instant.now().plus(Duration.ofDays(5));
        var mockTourCompany = new TourCompany(1, "mock tour",TourCompanyStatus.APPROVED.name());
        when(tourCompanyRepository.findById(1)).thenReturn(Optional.of(mockTourCompany));

        var mockTour = new Tour(1, AggregateReference.to(mockTourCompany.id()), 
                                                        "Test create Tour", 
                                                        20, 
                                                        "description", 
                                                        "BKK", 
                                                        activityDate, 
                                                        TourStatus.PENDING.name());
        
        when(tourRepository.save(any(Tour.class))).thenReturn(mockTour);

        var mockTourCount = new TourCount(1, AggregateReference.to(mockTour.id()), 0);
        when(tourCountRepository.save(any(TourCount.class))).thenReturn(mockTourCount);

        var crateTourDTO = new CreateTourDto(1, 
                                        "Test create Tour", 
                                        20, 
                                        "description", 
                                        "BKK", 
                                        activityDate);
        var actual = tourServiceImp.createTour(crateTourDTO);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1, actual.id().intValue());
        Assertions.assertEquals("Test create Tour", actual.title());
    }

    @Test
    void shouldThrowErrorWhenNotFoundedTourCompany() {
        when(tourCompanyRepository.findById(anyInt())).thenReturn(Optional.empty());
        var exception = Assertions.assertThrows(EntityNotFound.class, 
                        () -> tourServiceImp.createTour(new CreateTourDto(1, 
                                                            "Test create Tour", 
                                                            20, 
                                                            "description", 
                                                            "BKK", 
                                                            Instant.now().plus(Duration.ofDays(5)))));
        Assertions.assertEquals(String.format("tourCompany id: %d not found", 1), 
                        exception.getMessage());
    }

    @Test
    void shouldReturnTourWhenGetTourById() {
        var activityDate = Instant.now().plus(Duration.ofDays(5));
        var mockTour = new Tour(1, 
                        AggregateReference.to(1),
                        "Test get tour by id", 
                        20,
                        "description", 
                        "Bkk" , 
                        activityDate, 
                        TourStatus.PENDING.name());
        when(tourRepository.findById(1)).thenReturn(Optional.of(mockTour));

        var actual = tourServiceImp.gettourById(1);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(mockTour.id().intValue(), actual.id().intValue());
        Assertions.assertEquals(mockTour.tour_company_id().getId(), actual.tour_company_id().getId());
        Assertions.assertEquals(mockTour.number_of_people(), actual.number_of_people());
        Assertions.assertEquals(mockTour.description(), actual.description());
    }

    @Test
    void shouldThrowErrorWhenNotfoundTour() {
        when(tourRepository.findById(anyInt())).thenReturn(Optional.empty());
        var exception = Assertions.assertThrows(EntityNotFound.class,
        ()-> tourServiceImp.gettourById(1));
        Assertions.assertEquals(String.format("Tour id: %d not found", 1), 
        exception.getMessage());
    }

    @Test
    void shouldReturnPageOfToursWhenGetAllTours() {
        var mockTour = new Tour(1, 
                        AggregateReference.to(1),
                        "Test get tour by id", 
                        20,
                        "description", 
                        "Bkk" , 
                        Instant.now().plus(Duration.ofDays(5)), 
                        TourStatus.PENDING.name());
        var mockPage = new PageImpl<>(List.of(mockTour));
        when(tourRepository.findAll(any(Pageable.class))).thenReturn(mockPage);
        var actual = tourServiceImp.getPageTour(PageRequest.of(1, 10));
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(!actual.getContent().isEmpty());
        Assertions.assertEquals(1, actual.getTotalElements());
    }

}
