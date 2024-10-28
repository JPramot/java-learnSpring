package dev.lpa.goutbackend.tour;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/tour")
public class TourController {

    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);

    private final Map<Integer,Tour> tourTemp;
    private final Logger logger = LoggerFactory.getLogger(TourController.class);

    public TourController(){
        this.tourTemp = new HashMap<>();
    }

    //| CRUD
    //* Get ALL 
    @GetMapping
    public List<Tour> getTours() {

        logger.info("Get all tour");
        return List.copyOf(tourTemp.values());
    }
    //* Get by id */
    @GetMapping("/{id}")
    public Tour getTourById(@PathVariable int id) {
    logger.info("Get tour: {}", id);
    return Optional.ofNullable(tourTemp.get(id))
            .orElseThrow(() -> {
                logger.error("Tour ID: {} not founded", id);  // แก้ไขการใช้ logger.error
                return new RuntimeException("Not found");
            });
    }
    //* Create tour */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tour createTour(@RequestBody Tour tour) {
        Tour newTour = new Tour(ATOMIC_INTEGER.incrementAndGet(),tour.title(),tour.maxPeople());
        Integer id = newTour.id();
        tourTemp.put(id, newTour);
        logger.info("Create tour: {}",tourTemp.get(id));
        return getTourById(id);
    }
    //* Update tour */
    @PutMapping("/{id}")
    public Tour updateTour(@PathVariable int id, @RequestBody Tour tour) {
        Tour updatedTour = new Tour(id,tour.title(),tour.maxPeople());
        tourTemp.put(id, updatedTour);
        logger.info("update tour: {}",tourTemp.get(id));
        return getTourById(id);
    }
    //* Delete tour by id */
    @DeleteMapping("/{id}")
    public String deleteTour(@PathVariable int id) {
        if(!tourTemp.containsKey(id)) {
            logger.error("Tour ID: {} not founded", id);
            return "not found tour";
        }else {
            tourTemp.remove(id);
            logger.info("Delete tour: {} success",id);
            return "success";
        }
    }



}
