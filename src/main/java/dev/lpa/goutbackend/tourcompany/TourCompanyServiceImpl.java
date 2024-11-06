package dev.lpa.goutbackend.tourcompany;

import java.time.Instant;
import java.math.BigDecimal;

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
import dev.lpa.goutbackend.wallet.models.TourCompanyWallet;
import dev.lpa.goutbackend.wallet.repo.TourCompanyWalletRepository;

@Service
public class TourCompanyServiceImpl implements TourCompanyService {

    private final TourCompanyRepository tourCompanyRepository;
    private final TourCompanyLoginRepository tourCompanyLoginRepository;
    private final TourCompanyWalletRepository tourCompanyWalletRepository;
    private final PasswordEncoder passwordEncoder;

    private final Logger logger = LoggerFactory.getLogger(TourCompanyServiceImpl.class);

    public TourCompanyServiceImpl(TourCompanyRepository tourCompanyRepository,
            TourCompanyLoginRepository tourCompanyLoginRepository,
            PasswordEncoder passwordEncoder,
            TourCompanyWalletRepository tourCompanyWalletRepository) {
        this.tourCompanyWalletRepository = tourCompanyWalletRepository;
        this.passwordEncoder = passwordEncoder;
        this.tourCompanyLoginRepository = tourCompanyLoginRepository;
        this.tourCompanyRepository = tourCompanyRepository;
    }

    @Override
    @Transactional
    public TourCompany registerTourCompany(RegisterTourCompanyDto payload) {
        logger.debug("[registerTour] newly tour company is registing...");
        //NOTE: create new tourCompany
        TourCompany tourCompany = new TourCompany(
                    null,
                    payload.name(),
                    TourCompanyStatus.WAITING.name());
        var newTourCompany = tourCompanyRepository.save(tourCompany);
        logger.debug("registerTour newTourCompany: {}", newTourCompany);

        //NOTE: create new tourCompanyLogin
        createTourCompanyCredential(newTourCompany, payload);
        return newTourCompany;
    }

    private void createTourCompanyCredential(TourCompany tourCompany, RegisterTourCompanyDto payload) {
        String encryptedPassword = passwordEncoder.encode(payload.password());
        AggregateReference<TourCompany, Integer> tourCompanyRef = AggregateReference.to(tourCompany.id());
        var tourCompanyLogin = tourCompanyLoginRepository.save(new TourCompanyLogin(null, tourCompanyRef, payload.userName(), encryptedPassword));
        logger.info("create TourCompanyLogin: {} for tourCompany: {}",tourCompanyLogin.id(),tourCompany.id());
    }

    @Override
    @Transactional
    public TourCompany approveTourCompany(Integer id) {
        //NOTE: approve tourCompany
        TourCompany tourCompany = tourCompanyRepository.findById(id)
                                    .orElseThrow(()->new EntityNotFound(String.format("tourCompany id: %d not found",id)));
        tourCompany = new TourCompany(id, tourCompany.name(), TourCompanyStatus.APPROVED.name());

        //NOTE: create tourCompanyWallet
        createTourCompanyWallet(tourCompany);
        return tourCompanyRepository.save(tourCompany);
    }

    private void createTourCompanyWallet(TourCompany tourCompany) {
        AggregateReference<TourCompany, Integer> tourCompanyRef = AggregateReference.to(tourCompany.id());
        Instant now = Instant.now();
        BigDecimal balance = new BigDecimal(0);
        tourCompanyWalletRepository.save(new TourCompanyWallet(null, tourCompanyRef, now, balance));
        logger.info("create tourCompanyWallet for tourCompany: {}", tourCompany.id());
    }

    @Override
    public TourCompany getTourCompanyById(Integer id) {
        return tourCompanyRepository.findById(id).
                orElseThrow(()->new EntityNotFound("entity not founded"));
    }
    

}
