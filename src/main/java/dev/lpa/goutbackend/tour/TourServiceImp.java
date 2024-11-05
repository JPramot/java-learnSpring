package dev.lpa.goutbackend.tour;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.lpa.goutbackend.commons.enumulation.TourStatus;
import dev.lpa.goutbackend.commons.exceptions.EntityNotFound;
import dev.lpa.goutbackend.tour.dtos.CreateTourDto;
import dev.lpa.goutbackend.tour.models.Tour;
import dev.lpa.goutbackend.tour.models.TourCount;
import dev.lpa.goutbackend.tour.repo.TourCountRepository;
import dev.lpa.goutbackend.tour.repo.TourRepository;
import dev.lpa.goutbackend.tourcompany.models.TourCompany;
import dev.lpa.goutbackend.tourcompany.repo.TourCompanyRepository;

@Service
public class TourServiceImp implements TourService {

    private final TourRepository tourRepository;
    private final TourCountRepository tourCountRepository;
    private final TourCompanyRepository tourCompanyRepository;

    private final Logger logger = LoggerFactory.getLogger(TourServiceImp.class);

    public TourServiceImp(TourRepository tourRepository,
        TourCountRepository tourCountRepository,
        TourCompanyRepository tourCompanyRepository) {
        this.tourCompanyRepository=tourCompanyRepository;
        this.tourCountRepository=tourCountRepository;
        this.tourRepository = tourRepository;
    }

    @Override
    @Transactional
    public Tour createTour(CreateTourDto tour) {

        TourCompany exist = tourCompanyRepository.findById(tour.tour_company_id())
                                .orElseThrow(()->new EntityNotFound(String.format("tourCompany id: %d not found", 
                                                                        tour.tour_company_id())));
                
        AggregateReference<TourCompany,Integer> tourCompanyRef = AggregateReference.to(exist.id());        
        Tour newTour = tourRepository.save(new Tour(null,
            tourCompanyRef,
            tour.title(), 
            tour.number_of_people(), 
            tour.description(), 
            tour.location(), 
            tour.activity_date(), 
            TourStatus.PENDING.name()));
        logger.debug("tour was create with id: {}",newTour.id());

        tourCountRepository.save(new TourCount(null, AggregateReference.to(newTour.id()), 0));
    
        return newTour;
    }

    @Override
    public Page<Tour> getPageTour(Pageable pageable) {
        return tourRepository.findAll(pageable);
    }

    @Override
    public Tour gettourById(int id) {
        return tourRepository.findById(id)
                .orElseThrow(()->new EntityNotFound(String.format("Tour id: %d not found", id)));
    }
    

}
