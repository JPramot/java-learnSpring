package dev.lpa.goutbackend.user.repo;


import org.springframework.data.repository.CrudRepository;

import dev.lpa.goutbackend.user.models.User;


public interface UserRepository extends CrudRepository<User,Integer> {

}
