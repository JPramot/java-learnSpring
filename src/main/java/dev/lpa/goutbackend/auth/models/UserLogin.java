package dev.lpa.goutbackend.auth.models;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_login")
public record UserLogin(
    @Id Integer id,
    AggregateReference<User,Integer> userId,
    String email,
    String password
) {

}
