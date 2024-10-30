package dev.lpa.goutbackend.tourcompany;

import dev.lpa.goutbackend.tourcompany.dtos.RegisterTourCompanyDto;
import dev.lpa.goutbackend.tourcompany.models.TourCompany;

public interface TourCompanyService {

    TourCompany registerTourCompany(RegisterTourCompanyDto payload);

    TourCompany approveTourCompany(Integer id);

    TourCompany getTourCompanyById(Integer id);
}
