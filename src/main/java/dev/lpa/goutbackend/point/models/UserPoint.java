package dev.lpa.goutbackend.point.models;

import java.time.Instant;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_point")
public record UserPoint(
    @Id Integer id,
    AggregateReference<User, Integer> userId,
    Instant lastUpdated,
    Integer point

) {

}
