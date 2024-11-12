package dev.lpa.goutbackend.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.security.crypto.password.PasswordEncoder;

import dev.lpa.goutbackend.auth.models.UserLogin;
import dev.lpa.goutbackend.auth.repo.UserLoginRepository;
import dev.lpa.goutbackend.commons.exceptions.EntityNotFound;
import dev.lpa.goutbackend.user.dtos.CreateUserLoginDto;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Spy
    @InjectMocks
    private AuthServiceImp authServiceImp;

    @Mock
    private UserLoginRepository userLoginRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldReturnUserLoginWhenFindByEmail() {

        var mockUserLogin = new UserLogin(1, AggregateReference.to(1),
                "john@gmail.com", "123456");

        when(userLoginRepository
                .findOneByEmail(anyString())).thenReturn(Optional.of(mockUserLogin));

        var actual = authServiceImp.findCredentialByEmail("john@gmail.com");

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(mockUserLogin.id(), actual.get().id());
        Assertions.assertEquals(mockUserLogin.userId(), actual.get().userId());
        Assertions.assertEquals(mockUserLogin.email(), actual.get().email());
    }

    @Test
    void shouldCreateUserLoginIfSuccess() {

        var mockUserLogin = new UserLogin(1, AggregateReference.to(1),
                "john@gmail.com", "encryptedPassword");

        when(passwordEncoder.encode(anyString())).thenReturn("encryptedPassword");
        when(userLoginRepository.save(any(UserLogin.class))).thenReturn(mockUserLogin);

        var createUserLoginDto = new CreateUserLoginDto(1, "john@gmail.com", "123456");
        var actual = authServiceImp.createUserLogin(createUserLoginDto);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(mockUserLogin.id(), actual.id());
        Assertions.assertEquals(mockUserLogin.userId(), actual.userId());
        Assertions.assertEquals(mockUserLogin.email(), actual.email());
        Assertions.assertEquals(mockUserLogin.password(), actual.password());
    }

    @Test
    void shouldReturnUserLoginWhenFindByUserId() {

        var mockUserLogin = new UserLogin(1, AggregateReference.to(1),
                "john@gmail.com", "123456");

        when(userLoginRepository
                .findByUserId(AggregateReference.to(1))).thenReturn(Optional.of(mockUserLogin));

        var actual = authServiceImp.findCredentialByUserId(1);

        Assertions.assertNotNull(actual.get());
        Assertions.assertEquals(mockUserLogin.id(), actual.get().id());
        Assertions.assertEquals(mockUserLogin.userId(), actual.get().userId());
        Assertions.assertEquals(mockUserLogin.email(), actual.get().email());
        Assertions.assertEquals(mockUserLogin.password(), actual.get().password());
    }

    @Test
    void shouldDeleteUserLoginIfSuccess() {
        var mockUserLogin = new UserLogin(1, AggregateReference.to(1),
                "john@gmail.com", "123456");

        when(authServiceImp.findCredentialByUserId(anyInt()))
                .thenReturn(Optional.of(mockUserLogin));
        authServiceImp.deleteUserCredentialByUserId(1);

        verify(userLoginRepository, times(1)).delete(any(UserLogin.class));

    }

    @Test
    void shouldThorwErrorWhenDeleteUserLoginButNotFound() {

        // when(authServiceImp.findCredentialByUserId(1))
        // .thenReturn(Optional.empty());

        // | AggregateReference ต้องการค่าที่เฉพาะเจาะจง ใช้ matcher ไม่ได้
        when(userLoginRepository.findByUserId(AggregateReference.to(1))).thenReturn(Optional.empty());

        var exception = Assertions.assertThrows(EntityNotFound.class,
                () -> authServiceImp.deleteUserCredentialByUserId(1));

        Assertions.assertEquals("userLogin with userId: 1 not found", exception.getMessage());
    }
}
