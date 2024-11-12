package dev.lpa.goutbackend.auth;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.lpa.goutbackend.auth.models.UserLogin;
import dev.lpa.goutbackend.auth.repo.UserLoginRepository;
import dev.lpa.goutbackend.commons.exceptions.EntityNotFound;
import dev.lpa.goutbackend.user.dtos.CreateUserLoginDto;

@Service
public class AuthServiceImp implements AuthService {

    private final UserLoginRepository userLoginRepository;
    private final PasswordEncoder passwordEncoder;

    private final Logger logger = LoggerFactory.getLogger(AuthServiceImp.class);

    public AuthServiceImp(UserLoginRepository userLoginRepository,
            PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userLoginRepository = userLoginRepository;
    }

    @Override
    public Optional<UserLogin> findCredentialByEmail(String email) {
        return userLoginRepository.findOneByEmail(email);
    }

    @Override
    public UserLogin createUserLogin(CreateUserLoginDto userLoginDto) {
        String encryptedPassword = passwordEncoder.encode(userLoginDto.password());
        UserLogin userLogin = new UserLogin(null,
                AggregateReference.to(userLoginDto.userId()),
                userLoginDto.email(),
                encryptedPassword);

        var savedUserLogin = userLoginRepository.save(userLogin);
        logger.debug("create UserLogin with userId: {}", userLoginDto.userId());
        return savedUserLogin;
    }

    @Override
    public Optional<UserLogin> findCredentialByUserId(int userId) {

        return userLoginRepository.findByUserId(AggregateReference.to(userId));
    }

    @Override
    public void deleteUserCredentialByUserId(int userId) {
        var existUserLogin = findCredentialByUserId(userId)
                .orElseThrow(() -> new EntityNotFound(String.format("userLogin with userId: %d not found", userId)));

        userLoginRepository.delete(existUserLogin);
        logger.debug("delete userLogin with userId: {}", userId);
    }

}
