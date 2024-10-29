package dev.lpa.goutbackend.tourcompany.models;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Table;

@Table("tour_company_wallet")
public record TourCompanyWallet(
    @Id Integer id,
    AggregateReference<TourCompany,Integer> tour_company_id,
    Instant last_updated,
    BigDecimal balance
) {
} 


