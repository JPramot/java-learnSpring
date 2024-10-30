package dev.lpa.goutbackend.tour.repo;

import org.springframework.data.repository.CrudRepository;

import dev.lpa.goutbackend.tour.models.TourCount;

public interface TourCountRepository extends CrudRepository<TourCount,Integer> {

}
