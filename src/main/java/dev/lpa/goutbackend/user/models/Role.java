package dev.lpa.goutbackend.user.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("role")
public record Role(@Id Integer id, String name) {

}
