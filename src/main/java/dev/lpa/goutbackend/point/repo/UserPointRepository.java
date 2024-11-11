package dev.lpa.goutbackend.point.repo;

import org.springframework.data.repository.ListCrudRepository;

import dev.lpa.goutbackend.point.models.UserPoint;

public interface UserPointRepository extends ListCrudRepository<UserPoint, Integer> {

    
} 
