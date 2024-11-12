package dev.lpa.goutbackend.user.repo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import dev.lpa.goutbackend.user.models.User;

public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findByPhoneNumber(String phoneNumber);
}
