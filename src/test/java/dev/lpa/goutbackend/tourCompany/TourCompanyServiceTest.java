package dev.lpa.goutbackend.tourCompany;

import java.util.Optional;
import java.time.Instant;
import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.security.crypto.password.PasswordEncoder;

import dev.lpa.goutbackend.commons.enumulation.TourCompanyStatus;
import dev.lpa.goutbackend.commons.exceptions.EntityNotFound;
import dev.lpa.goutbackend.tourcompany.TourCompanyServiceImpl;
import dev.lpa.goutbackend.tourcompany.dtos.RegisterTourCompanyDto;
import dev.lpa.goutbackend.tourcompany.models.TourCompany;
import dev.lpa.goutbackend.tourcompany.models.TourCompanyLogin;
import dev.lpa.goutbackend.tourcompany.repo.TourCompanyLoginRepository;
import dev.lpa.goutbackend.tourcompany.repo.TourCompanyRepository;
import dev.lpa.wallet.models.TourCompanyWallet;
import dev.lpa.wallet.repo.TourCompanyWalletRepository;

@ExtendWith(MockitoExtension.class)
class TourCompanyServiceTest {

    @InjectMocks
    private TourCompanyServiceImpl tourCompanyService;

    @Mock
    private TourCompanyRepository tourCompanyRepository;

    @Mock
    private TourCompanyLoginRepository tourCompanyLoginRepository;

    @Mock
    private TourCompanyWalletRepository tourCompanyWalletRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldRegisterTourCompany() {
        var mockTourCompany = new TourCompany(1, "Test Create", TourCompanyStatus.WAITING.name());

        when(tourCompanyRepository.save(any(TourCompany.class))).thenReturn(mockTourCompany);

        when(passwordEncoder.encode(anyString())).thenReturn("encryptedPassword");

        var tourCompanyCredential = new TourCompanyLogin(1, 
                AggregateReference.to(1), "gout", "encryptedPassword");
        when(tourCompanyLoginRepository.save(any(TourCompanyLogin.class)))
                .thenReturn(tourCompanyCredential);
        
        RegisterTourCompanyDto payload = new RegisterTourCompanyDto(
            null, "Test create", "gout", "123456", null);
        var actual = tourCompanyService.registerTourCompany(payload);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1, actual.id().intValue());
        Assertions.assertEquals("Test Create", actual.name());
        Assertions.assertEquals(TourCompanyStatus.WAITING.name(), actual.status());

    }

    @Test
    void shouldApproveTourCompony() {
        var mockTourCompany = new TourCompany(1, "test approve", TourCompanyStatus.WAITING.name());
        when(tourCompanyRepository.findById(1))
                .thenReturn(Optional.of(mockTourCompany));

        mockTourCompany = new TourCompany(1, "test approve", TourCompanyStatus.APPROVED.name());
        when(tourCompanyRepository.save(any(TourCompany.class))).thenReturn(mockTourCompany);

        var mockWallet = new TourCompanyWallet(1, AggregateReference.to(1), Instant.now(), new BigDecimal("0.00"));
        when(tourCompanyWalletRepository.save(any(TourCompanyWallet.class))).thenReturn(mockWallet);

        var actual = tourCompanyService.approveTourCompany(1);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1, actual.id().intValue());
        Assertions.assertEquals("test approve", actual.name());
        Assertions.assertEquals(TourCompanyStatus.APPROVED.name(), actual.status());

    }

    @Test
    void shouldThrowErrorWhenApproveButNotFoundedTourcompany() {
        
        when(tourCompanyRepository.findById(anyInt())).thenReturn(Optional.empty());
        var exception = Assertions.assertThrows(EntityNotFound.class, 
                            () -> tourCompanyService.approveTourCompany(1));
        Assertions.assertEquals(String.format("tourCompany id: %d not found", 1), 
                            exception.getMessage());
    }

    @Test 
    void shouldReturnTourCompanyWhenGetTourCompanyById() {
        TourCompany mockTourCompany = new TourCompany(1, "test get", TourCompanyStatus.APPROVED.name());
        when(tourCompanyRepository.findById(1)).thenReturn(Optional.of(mockTourCompany));

        var actual = tourCompanyService.getTourCompanyById(1);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1, actual.id().intValue());
        Assertions.assertEquals("test get", actual.name());
    }

    @Test
    void shouldThrowErrorWhenNotFoundedTourCompany() {
        
         // Arrange
        Integer id = 2;
        when(tourCompanyRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFound exception = assertThrows(EntityNotFound.class, () -> {
            tourCompanyService.getTourCompanyById(id);
        });
        assertEquals("entity not founded", exception.getMessage());
    }


}
