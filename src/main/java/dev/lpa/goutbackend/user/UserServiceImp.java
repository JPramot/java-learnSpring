package dev.lpa.goutbackend.user;

import java.math.BigDecimal;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import dev.lpa.goutbackend.auth.models.UserLogin;
import dev.lpa.goutbackend.auth.repo.UserLoginRepository;
import dev.lpa.goutbackend.commons.exceptions.EntityNotFound;
import dev.lpa.goutbackend.point.models.UserPoint;
import dev.lpa.goutbackend.point.repo.UserPointRepository;
import dev.lpa.goutbackend.user.dtos.CreateUserDto;
import dev.lpa.goutbackend.user.dtos.CreateUserLoginDto;
import dev.lpa.goutbackend.user.dtos.CreateUserPointDto;
import dev.lpa.goutbackend.user.dtos.CreateUserWalletDto;
import dev.lpa.goutbackend.user.dtos.UpdateUserDto;
import dev.lpa.goutbackend.user.models.User;
import dev.lpa.goutbackend.user.repo.UserRepository;
import dev.lpa.goutbackend.wallet.models.UserWallet;
import dev.lpa.goutbackend.wallet.repo.UserWalletRepository;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final UserLoginRepository userLoginRepository;
    private final UserPointRepository userPointRepository;
    private final UserWalletRepository userWalletRepository;
    private final PasswordEncoder passwordEncoder;

    private final Logger logger = LoggerFactory.getLogger(UserServiceImp.class);

    public UserServiceImp(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserLoginRepository userLoginRepository,
            UserPointRepository userPointRepository,
            UserWalletRepository userWalletRepository) {
        this.userWalletRepository = userWalletRepository;
        this.userPointRepository = userPointRepository;
        this.userLoginRepository = userLoginRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public User createUser(CreateUserDto createUserDto) {
        logger.debug("creteUser service with body: {}", createUserDto);
        Instant time = Instant.now();
        User user = new User(null, 
                        createUserDto.firstName(), 
                        createUserDto.lastName(), 
                        createUserDto.phoneNumber());
        User newUser = userRepository.save(user);
        CreateUserLoginDto userLoginDto  = new CreateUserLoginDto(newUser.id(), 
                                                createUserDto.email(), 
                                                createUserDto.password());
        CreateUserPointDto userPointDto = new CreateUserPointDto(newUser.id(), time);
        CreateUserWalletDto userWalletDto = new CreateUserWalletDto(newUser.id(), time);
        logger.info("{}",userLoginDto);
        createUserLogin(userLoginDto);
        createUserPoint(userPointDto);
        createUserWallet(userWalletDto);
        logger.info("user userPoint userWallet userLogin was created with userId: {}", newUser.id());
        return newUser;
    }

    private void createUserLogin(@Validated CreateUserLoginDto userLoginDto) {
        String encryptedPassword = passwordEncoder.encode(userLoginDto.password());
        UserLogin userLogin = new UserLogin(null, 
                                    AggregateReference.to(userLoginDto.userId()), 
                                    userLoginDto.email(), 
                                    encryptedPassword);
        logger.info("create UserLogin with body: {}", userLogin);
        userLoginRepository.save(userLogin);
        logger.debug("create userLogin with userId: {}", userLoginDto.userId());
    }

    private void createUserPoint(@Validated CreateUserPointDto userPointDto) {
        UserPoint userPoint = new UserPoint(null, 
                                    AggregateReference.to(userPointDto.userId()), 
                                    userPointDto.lastUpdated(), 
                                    0);
        userPointRepository.save(userPoint);
        logger.debug("create userPoint with userId: {}", userPointDto.userId());
    }

    private void createUserWallet(@Validated CreateUserWalletDto userWalletDto) {
        BigDecimal balance = new BigDecimal(0);
        UserWallet userWallet = new UserWallet(null, 
                                    AggregateReference.to(userWalletDto.userId()), 
                                    userWalletDto.lastUpdated(),
                                    balance );

        userWalletRepository.save(userWallet);
        logger.debug("create userWallet with userId: {}", userWalletDto.userId());
    }

    @Override
    public User findUserById(Integer id) {
        logger.debug("findUserById service with id: {}", id);
        User user = userRepository.findById(id)
                        .orElseThrow(()->new EntityNotFound(String
                                                            .format("user id: %d not found", id)));
        logger.info("found user with id: {}", user.id());
        return user;
    }

    @Override
    public User updateUserById(Integer id, UpdateUserDto updateUserDto) {
        logger.debug("updateUserById service with id: {}", id);
        User existUser = userRepository.findById(id)
                            .orElseThrow(()-> new EntityNotFound(String
                                                    .format("user id: %d not found", id)));
        existUser = new User(existUser.id(), 
                        updateUserDto.firstName(), 
                        updateUserDto.lastName(), 
                        updateUserDto.phoneNumber());
        User updateUser = userRepository.save(existUser);
        logger.info("update user with id: {}", updateUser.id());
        return updateUser;
    }

    @Override
    public void deleteUserById(Integer id) {
        logger.debug("deleteUserById service with id: {}", id);

        userRepository.deleteById(id);
        logger.info("deleted user with id: {}", id);
        
    }

    

}
