package dev.lpa.goutbackend.tourcompany;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.lpa.goutbackend.commons.enumulation.TourCompanyStatus;
import dev.lpa.goutbackend.commons.exceptions.EntityNotFound;
import dev.lpa.goutbackend.tourcompany.dtos.RegisterTourCompanyDto;
import dev.lpa.goutbackend.tourcompany.models.TourCompany;
import dev.lpa.goutbackend.tourcompany.models.TourCompanyLogin;
import dev.lpa.goutbackend.tourcompany.repo.TourCompanyLoginRepository;
import dev.lpa.goutbackend.tourcompany.repo.TourCompanyRepository;

@Service
public class TourCompanyServiceImpl implements TourCompanyService {

    private final TourCompanyRepository tourCompanyRepository;
    private final TourCompanyLoginRepository tourCompanyLoginRepository;
    private final PasswordEncoder passwordEncoder;

    private final Logger logger = LoggerFactory.getLogger(TourCompanyServiceImpl.class);

    public TourCompanyServiceImpl(TourCompanyRepository tourCompanyRepository,
            TourCompanyLoginRepository tourCompanyLoginRepository,
            PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.tourCompanyLoginRepository = tourCompanyLoginRepository;
        this.tourCompanyRepository = tourCompanyRepository;
    }

    @Override
    @Transactional
    public TourCompany registerTourCompany(RegisterTourCompanyDto payload) {
        logger.debug("[registerTour] newly tour company is registing...");
        TourCompany tourCompany = new TourCompany(
                    null,
                    payload.name(),
                    TourCompanyStatus.WAITING.name());
        var newTourCompany = tourCompanyRepository.save(tourCompany);
        logger.debug("registerTour newTourCompany: {}", newTourCompany);

        createTourCompanyCredential(newTourCompany, payload);
        return newTourCompany;
    }

    private void createTourCompanyCredential(TourCompany tourCompany, RegisterTourCompanyDto payload) {
        String encryptedPassword = passwordEncoder.encode(payload.password());
        AggregateReference<TourCompany, Integer> tourCompanyRef = AggregateReference.to(tourCompany.id());
        var tourCompanyLogin = new TourCompanyLogin(null, tourCompanyRef, payload.userName(), encryptedPassword);
        tourCompanyLoginRepository.save(tourCompanyLogin);
        logger.info("create TourCompanyLogin: {} for tourCompany: {}",tourCompanyLogin.id(),tourCompany.id());
    }

    @Override
    public TourCompany findTourCompanyById(Integer id) {
        return tourCompanyRepository.findById(id).orElseThrow(()->new EntityNotFound("tourCompany not founded"));
    }

    @Override
    public TourCompany updateTourCompany(RegisterTourCompanyDto tourCompany) {
        TourCompany newTourCompany = new TourCompany(tourCompany.id(),
                                 tourCompany.name(),
                                 TourCompanyStatus.APPROVED.name());
        return tourCompanyRepository.save(newTourCompany);
    }

    @Override
    public TourCompany approveTourCompany(Integer id) {
        TourCompany tourCompany = tourCompanyRepository.findById(id)
                                    .orElseThrow(()->new EntityNotFound(String.format("tourCompany id: %d not found",id)));
        tourCompany = new TourCompany(id, tourCompany.name(), TourCompanyStatus.APPROVED.name());
        return tourCompanyRepository.save(tourCompany);
    }

    

    

}
