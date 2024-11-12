package dev.lpa.goutbackend.auth;

import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.lpa.goutbackend.auth.models.UserLogin;
import dev.lpa.goutbackend.user.dtos.CreateUserLoginDto;

@Service
public interface AuthService {
    Optional<UserLogin> findCredentialByEmail(String email);

    Optional<UserLogin> findCredentialByUserId(int userId);

    UserLogin createUserLogin(CreateUserLoginDto userLoginDto);

    void deleteUserCredentialByUserId(int userId);
}
