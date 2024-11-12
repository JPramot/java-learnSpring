package dev.lpa.goutbackend.user;

import java.time.Instant;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.lpa.goutbackend.auth.AuthService;
import dev.lpa.goutbackend.commons.exceptions.CredentialExistException;
import dev.lpa.goutbackend.commons.exceptions.EntityNotFound;
import dev.lpa.goutbackend.point.UserPointService;
import dev.lpa.goutbackend.user.dtos.CreateUserDto;
import dev.lpa.goutbackend.user.dtos.CreateUserLoginDto;
import dev.lpa.goutbackend.user.dtos.CreateUserPointDto;
import dev.lpa.goutbackend.user.dtos.CreateUserWalletDto;
import dev.lpa.goutbackend.user.dtos.UpdateUserDto;
import dev.lpa.goutbackend.user.models.User;
import dev.lpa.goutbackend.user.repo.UserRepository;
import dev.lpa.goutbackend.wallet.UserWalletService;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final UserWalletService userWalletService;
    private final UserPointService userPointService;

    private final Logger logger = LoggerFactory.getLogger(UserServiceImp.class);

    public UserServiceImp(UserRepository userRepository,
            AuthService authService,
            UserWalletService userWalletService,
            UserPointService userPointService) {
        this.userPointService = userPointService;
        this.userWalletService = userWalletService;
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public User createUser(CreateUserDto createUserDto) {
        logger.debug("creteUser service with body: {}", createUserDto);
        // NOTE: check if user exist throw exception
        var existUserCredential = authService.findCredentialByEmail(createUserDto.email());
        if (existUserCredential.isPresent()) {
            throw new CredentialExistException(String.format("user with email %s already exist",
                    createUserDto.email()));
        }
        var existPhone = findUserByPhone(createUserDto.phoneNumber());
        if (existPhone.isPresent()) {
            throw new CredentialExistException(String.format("user with phone %s already exist",
                    createUserDto.phoneNumber()));
        }
        Instant time = Instant.now();
        // NOTE: create new user
        User user = new User(null,
                createUserDto.firstName(),
                createUserDto.lastName(),
                createUserDto.phoneNumber());
        User newUser = userRepository.save(user);
        // NOTE: create new userLogin
        CreateUserLoginDto userLoginDto = new CreateUserLoginDto(newUser.id(),
                createUserDto.email(),
                createUserDto.password());
        authService.createUserLogin(userLoginDto);
        // NOTE: create new userPoint
        CreateUserPointDto userPointDto = new CreateUserPointDto(newUser.id(), time);
        userPointService.createUserPoint(userPointDto);
        // NOTE: create new userWallet
        CreateUserWalletDto userWalletDto = new CreateUserWalletDto(newUser.id(), time);
        userWalletService.createUserWallet(userWalletDto);

        logger.info("create user and all related entities with userId: {}", newUser.id());
        return newUser;
    }

    private Optional<User> findUserByPhone(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public User findUserById(Integer id) {
        logger.debug("findUserById service with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFound(String
                        .format("user id: %d not found", id)));

        logger.info("found user with id: {}", user.id());
        return user;
    }

    @Override
    public User updateUserById(Integer id, UpdateUserDto updateUserDto) {
        logger.debug("updateUserById service with id: {}", id);
        // NOTE: find user
        User existUser = findUserById(id);
        // NOTE: update user info
        existUser = new User(existUser.id(),
                updateUserDto.firstName(),
                updateUserDto.lastName(),
                updateUserDto.phoneNumber());
        User updateUser = userRepository.save(existUser);

        logger.info("update user with id: {}", updateUser.id());
        return updateUser;
    }

    @Override
    @Transactional
    public void deleteUserById(Integer id) {
        logger.debug("deleteUserById service with id: {}", id);
        // NOTE: find user
        var existUser = findUserById(id);
        // NOTE: delete all related entities
        authService.deleteUserCredentialByUserId(existUser.id());
        userPointService.deleteUserPointByUserId(existUser.id());
        userWalletService.deleteUserWalletByUserId(existUser.id());

        logger.info("deleted user and all related entities with userId: {}", id);

    }

}
