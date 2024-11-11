package dev.lpa.goutbackend.auth.models;


import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Table;

import dev.lpa.goutbackend.user.models.User;

@Table("user_login")
public record UserLogin(
    @Id Integer id,
    AggregateReference<User,Integer> userId,
    String email,
    String password
) {

}
