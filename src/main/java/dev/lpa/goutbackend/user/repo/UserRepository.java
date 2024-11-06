package dev.lpa.goutbackend.user.repo;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.repository.ListCrudRepository;

public interface UserRepository extends ListCrudRepository<User,Integer> {

}
