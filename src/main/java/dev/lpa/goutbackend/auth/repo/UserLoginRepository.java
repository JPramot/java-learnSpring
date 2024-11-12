package dev.lpa.goutbackend.auth.repo;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

import dev.lpa.goutbackend.auth.models.UserLogin;

import dev.lpa.goutbackend.user.models.User;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

public interface UserLoginRepository extends ListCrudRepository<UserLogin, Integer> {

    Optional<UserLogin> findOneByEmail(String email);

    Optional<UserLogin> findByUserId(AggregateReference<User, Integer> userId);

}
