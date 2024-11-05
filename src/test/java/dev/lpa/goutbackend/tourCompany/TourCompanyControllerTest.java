package dev.lpa.goutbackend.tourCompany;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import com.fasterxml.jackson.databind.ObjectMapper;

import dev.lpa.goutbackend.commons.enumulation.TourCompanyStatus;
import dev.lpa.goutbackend.commons.exceptions.EntityNotFound;
import dev.lpa.goutbackend.tourcompany.TourCompanyController;
import dev.lpa.goutbackend.tourcompany.TourCompanyService;
import dev.lpa.goutbackend.tourcompany.dtos.RegisterTourCompanyDto;
import dev.lpa.goutbackend.tourcompany.models.TourCompany;

@WebMvcTest(TourCompanyController.class)
class TourCompanyControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TourCompanyService tourCompanyService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldRegisterTourCompany() {
        var mockTourCompany = new TourCompany(1, 
                "test create", 
                TourCompanyStatus.WAITING.name());
        
        var mockRegisterTourCompanyDto = new RegisterTourCompanyDto(null, 
                "test create", 
                "gout", 
                "123456", 
                null);

        when(tourCompanyService.registerTourCompany(any(RegisterTourCompanyDto.class)))
                .thenReturn(mockTourCompany);
        
        try {
            mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/tour-companies")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(mockRegisterTourCompanyDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1) )
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("test create"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(TourCompanyStatus.WAITING.name()));
        } catch (Exception e) {

            e.printStackTrace();
        }
        
    }

    @Test
    void shouldApproveTourCompany() {
        var mockTourCompany = new TourCompany(1, "test approve", TourCompanyStatus.APPROVED.name());

        when(tourCompanyService.approveTourCompany(anyInt())).thenReturn(mockTourCompany);

        try {
            mockMvc.perform(
                MockMvcRequestBuilders.patch(String.format("/api/v1/tour-companies/%d/approve", 1))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1) )
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("test approve"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(TourCompanyStatus.APPROVED.name()));
            

        } catch (Exception e){
            e.printStackTrace();
        }


    }

    @Test
    void shouldThrowEntityNotFoundWhenNotFoundedTourCompany() {
        when(tourCompanyService.approveTourCompany(anyInt())).thenThrow(new EntityNotFound());

        try {
            mockMvc.perform(
                MockMvcRequestBuilders.patch( String.format("/api/v1/tour-companies/%d/approve", 1))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
