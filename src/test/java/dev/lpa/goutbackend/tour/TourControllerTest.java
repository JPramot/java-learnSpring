package dev.lpa.goutbackend.tour;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.lpa.goutbackend.commons.enumulation.TourStatus;
import dev.lpa.goutbackend.commons.exceptions.EntityNotFound;
import dev.lpa.goutbackend.tour.dtos.CreateTourDto;
import dev.lpa.goutbackend.tour.models.Tour;

@WebMvcTest(TourController.class)
class TourControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TourService tourService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void whenCreateTourSuccess()  {
        var mockTour = new Tour(1,
                             AggregateReference.to(1),
                            "test create", 
                            10, 
                            "description", 
                            "BKK", 
                            Instant.now().plus(Duration.ofDays(5)), 
                            TourStatus.PENDING.name());
        
        when(tourService.createTour(any(CreateTourDto.class))).thenReturn(mockTour);

        var mockCreateTourDto = new CreateTourDto(1, 
                                "test create", 
                                10,
                                "description", 
                                "BKK", 
                                Instant.now().plus(Duration.ofDays(5)));

        try{
            mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/tours")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(mockCreateTourDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("test create"));
        }catch(Exception e){
            e.printStackTrace();
        }

        }

    @Test
    void whenGetTourByIdSuccess() throws Exception {
        var mockTour = new Tour(1,
                             AggregateReference.to(1),
                            "test get", 
                            10, 
                            "description", 
                            "BKK", 
                            Instant.now().plus(Duration.ofDays(5)), 
                            TourStatus.PENDING.name());

        when(tourService.gettourById(anyInt())).thenReturn(mockTour);

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/tours/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("test get"));
            
    }

    @Test
    void whenGetTourByIdButNotFound() throws Exception {
        when(tourService.gettourById(anyInt())).thenThrow(new EntityNotFound());

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/tours/1"))
            .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    void whenGetPageToursSuccess() throws Exception {
        var mockTour = new Tour(1,
                             AggregateReference.to(1),
                            "test create", 
                            10, 
                            "description", 
                            "BKK", 
                            Instant.now().plus(Duration.ofDays(5)), 
                            TourStatus.PENDING.name());
        var mockPage = new PageImpl<>(List.of(mockTour));

        when(tourService.getPageTour(any(Pageable.class))).thenReturn(mockPage);

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/tours?page=1&size=1&sortField=id&sortDirection=asc")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray());
    }

    @Test
    void whenGetPageTourButWrongArgument() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/tours"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


}
