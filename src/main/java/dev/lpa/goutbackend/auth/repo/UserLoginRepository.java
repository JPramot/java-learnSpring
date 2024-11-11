package dev.lpa.goutbackend.auth.repo;

import org.springframework.data.repository.ListCrudRepository;

import dev.lpa.goutbackend.auth.models.UserLogin;

public interface UserLoginRepository extends ListCrudRepository<UserLogin,Integer> {

}
