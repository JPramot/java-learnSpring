package dev.lpa.goutbackend.user.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_role")
public record UserRole(
    @Id Integer id,
    AggregateReference<User, Integer> usrId,
    AggregateReference<Role, Integer> roleId

) {

}
