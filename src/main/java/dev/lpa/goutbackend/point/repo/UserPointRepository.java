package dev.lpa.goutbackend.point.repo;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

import dev.lpa.goutbackend.point.models.UserPoint;
import dev.lpa.goutbackend.user.models.User;

import org.springframework.data.jdbc.core.mapping.AggregateReference;

public interface UserPointRepository extends ListCrudRepository<UserPoint, Integer> {

    Optional<UserPoint> findByUserId(AggregateReference<User, Integer> userId);

}
