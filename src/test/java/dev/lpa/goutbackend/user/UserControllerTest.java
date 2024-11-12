package dev.lpa.goutbackend.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
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

import dev.lpa.goutbackend.commons.exceptions.CredentialExistException;
import dev.lpa.goutbackend.commons.exceptions.EntityNotFound;
import dev.lpa.goutbackend.user.dtos.CreateUserDto;
import dev.lpa.goutbackend.user.dtos.UpdateUserDto;
import dev.lpa.goutbackend.user.models.User;

@WebMvcTest(UserController.class)
class UserControllerTest {

        @Autowired
        private WebApplicationContext webApplicationContext;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private UserService userService;

        private MockMvc mockMvc;

        @BeforeEach
        private void setup() {
                mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        }

        @Test
        void whenCreateUserSuccess() throws Exception {

                var mockUser = new User(1, "john",
                                "doe", "0000000000");
                when(userService.createUser(any(CreateUserDto.class))).thenReturn(mockUser);

                var createUserDto = new CreateUserDto("john", "doe",
                                "0000000000", "john@gmail.com", "123456");
                mockMvc.perform(
                                MockMvcRequestBuilders.post("/api/v1/users")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(createUserDto))

                ).andExpect(MockMvcResultMatchers.status().isCreated())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("john"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("doe"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("0000000000"));

        }

        @Test
        void whenCreateUserButPhoneAlreadyExist() throws Exception {

                when(userService.createUser(any(CreateUserDto.class)))
                                .thenThrow(new CredentialExistException(
                                                "user with phoneNumber 0000000000 already exist"));

                var createUserDto = new CreateUserDto("john", "doe",
                                "0000000000", "john@gmail.com", "123456");
                mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createUserDto)))
                                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.detail")
                                                .value("user with phoneNumber 0000000000 already exist"));
        }

        @Test
        void whenCreateUserButEmailAlreadyExist() throws Exception {

                when(userService.createUser(any(CreateUserDto.class)))
                                .thenThrow(new CredentialExistException());

                var createUserDto = new CreateUserDto("john", "doe",
                                "0000000000", "john@gmail.com", "123456");
                mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createUserDto)))
                                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void whenGetUserById() throws Exception {
                var mockUser = new User(1, "john",
                                "doe", "0000000000");
                when(userService.findUserById(anyInt())).thenReturn(mockUser);

                mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/1"))
                                .andExpect(MockMvcResultMatchers.status()
                                                .isOk())
                                .andExpect(MockMvcResultMatchers
                                                .jsonPath("$.id").value(1))
                                .andExpect(MockMvcResultMatchers
                                                .jsonPath("$.firstName").value("john"))
                                .andExpect(MockMvcResultMatchers
                                                .jsonPath("$.lastName").value("doe"))
                                .andExpect(MockMvcResultMatchers
                                                .jsonPath("$.phoneNumber").value("0000000000"));
        }

        @Test
        void whenGetUserByIdButNotfound() throws Exception {

                when(userService.findUserById(anyInt()))
                                .thenThrow(new EntityNotFound("user id: 1 not found"));

                mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/1"))
                                .andExpect(MockMvcResultMatchers.status().isNotFound())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.detail")
                                                .value("user id: 1 not found"));
        }

        @Test
        void whenUpdateUserById() throws Exception {
                var mockUpdateUser = new User(1, "joe",
                                "dee", "1111111111");

                // | ที่ต้องใส่ eq(1) เพราะ Mockito ต้องการให้ทุกอาร์กิวเมนต์ใช้ matcher ทั้งหมด
                // | หรือใช้ค่าโดยตรงทั้งหมด
                // | any(), eq() เป็น matcher
                when(userService.updateUserById(eq(1), any(UpdateUserDto.class)))
                                .thenReturn(mockUpdateUser);

                var updateUserDto = new UpdateUserDto("joe",
                                "dee", "1111111111");
                mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateUserDto)))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id")
                                                .value(1))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName")
                                                .value("joe"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName")
                                                .value("dee"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber")
                                                .value("1111111111"));
        }

        @Test
        void whenUpdateUserByIdButNotFoundUser() throws Exception {

                when(userService.updateUserById(eq(1), any(UpdateUserDto.class)))
                                .thenThrow(new EntityNotFound());

                var updateUserDto = new UpdateUserDto("jill",
                                "jim", "1212121212");
                mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateUserDto)))
                                .andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        @Test
        void whenDeleteUserById() throws Exception {

                mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/1"))
                                .andExpect(MockMvcResultMatchers.status().isNoContent());
        }

}
