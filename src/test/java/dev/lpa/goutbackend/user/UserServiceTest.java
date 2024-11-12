package dev.lpa.goutbackend.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

import dev.lpa.goutbackend.auth.AuthService;
import dev.lpa.goutbackend.auth.models.UserLogin;
import dev.lpa.goutbackend.commons.exceptions.CredentialExistException;
import dev.lpa.goutbackend.commons.exceptions.EntityNotFound;
import dev.lpa.goutbackend.point.UserPointService;
import dev.lpa.goutbackend.point.models.UserPoint;
import dev.lpa.goutbackend.user.dtos.CreateUserDto;
import dev.lpa.goutbackend.user.dtos.CreateUserLoginDto;
import dev.lpa.goutbackend.user.dtos.CreateUserPointDto;
import dev.lpa.goutbackend.user.dtos.CreateUserWalletDto;
import dev.lpa.goutbackend.user.dtos.UpdateUserDto;
import dev.lpa.goutbackend.user.models.User;
import dev.lpa.goutbackend.user.repo.UserRepository;
import dev.lpa.goutbackend.wallet.UserWalletService;
import dev.lpa.goutbackend.wallet.models.UserWallet;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

        @Spy
        @InjectMocks
        private UserServiceImp userService;

        @Mock
        private UserRepository userRepository;
        @Mock
        private UserWalletService userWalletService;
        @Mock
        private AuthService authService;
        @Mock
        private UserPointService userPointService;

        @Test
        void shouldCreateUserAndAllRelatedEntitiesIfSuccess() {

                when(authService.findCredentialByEmail(anyString())).thenReturn(Optional.empty());
                when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.empty());

                var mockUser = new User(1,
                                "john",
                                "doe",
                                "1234567890");

                when(userRepository.save(any(User.class))).thenReturn(mockUser);

                var mockUserLogin = new UserLogin(1,
                                AggregateReference.to(mockUser.id()),
                                "john@gmail.com",
                                "123456");
                when(authService.createUserLogin(any(CreateUserLoginDto.class))).thenReturn(mockUserLogin);

                var mockUserPoint = new UserPoint(1,
                                AggregateReference.to(mockUser.id()),
                                Instant.now(),
                                0);
                when(userPointService.createUserPoint(any(CreateUserPointDto.class))).thenReturn(mockUserPoint);

                var mockUserWallet = new UserWallet(1,
                                AggregateReference.to(mockUser.id()),
                                Instant.now(),
                                new BigDecimal(0));
                when(userWalletService.createUserWallet(any(CreateUserWalletDto.class))).thenReturn(mockUserWallet);

                var createUserDto = new CreateUserDto("john", "doe",
                                "1234567890", "john@gmail.com", "123456");
                var actual = userService.createUser(createUserDto);

                Assertions.assertNotNull(actual);
                Assertions.assertEquals(1, actual.id().intValue());
                Assertions.assertEquals(createUserDto.firstName(), actual.firstName());

        }

        @Test
        void shoulThrowErrorIfCredentialAlreadyExist() {
                var mockUserLogin = new UserLogin(1,
                                AggregateReference.to(1),
                                "john@gmail.com",
                                "1234567890");
                when(authService.findCredentialByEmail("john@gmail.com")).thenReturn(Optional.of(mockUserLogin));

                var createUserDto = new CreateUserDto("john", "doe",
                                "1234567890", "john@gmail.com", "123456");
                var exception = Assertions.assertThrows(CredentialExistException.class,
                                () -> userService.createUser(createUserDto));
                Assertions.assertEquals(String.format("user with email %s already exist",
                                createUserDto.email()), exception.getMessage());
        }

        @Test
        void shouldThrowErrorIfPhoneNumberAlreadyExist() {
                var mockUser = new User(1, "john", "doe", "1234567890");
                when(userRepository.findByPhoneNumber("1234567890")).thenReturn(Optional.of(mockUser));

                var createUserDto = new CreateUserDto("john", "doe",
                                "1234567890", "john@gmail.com", "123456");
                var exception = Assertions.assertThrows(CredentialExistException.class,
                                () -> userService.createUser(createUserDto));
                Assertions.assertEquals(String.format("user with phoneNumber %s already exist",
                                createUserDto.phoneNumber()), exception.getMessage());
        }

        @Test
        void shouldReturnUserWhenfindByIdSuccess() {

                var mockUser = new User(1, "john",
                                "doe", "1234567890");
                when(userRepository.findById(anyInt())).thenReturn(Optional.of(mockUser));

                var actual = userService.findUserById(1);
                Assertions.assertNotNull(actual);
                Assertions.assertEquals("john", actual.firstName());
                Assertions.assertEquals("doe", actual.lastName());
                Assertions.assertEquals("1234567890", actual.phoneNumber());
        }

        @Test
        void shouldThrowErrorIfnotFoundUser() {
                when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

                var exception = Assertions.assertThrows(EntityNotFound.class,
                                () -> userService.findUserById(1));

                Assertions.assertEquals(String.format("user id: %d not found", 1),
                                exception.getMessage());
        }

        @Test
        void shouldUpdateUserInfoIfSuccess() {
                var mockUser = new User(1, "john",
                                "doe", "1234567890");
                when(userRepository.findById(anyInt())).thenReturn(Optional.of(mockUser));

                var updateUser = new User(mockUser.id(), "jim",
                                "dee", "0000000000");
                when(userRepository.save(any(User.class))).thenReturn(updateUser);

                var updateUserDto = new UpdateUserDto("jim", "dee",
                                "0000000000");
                var actual = userService.updateUserById(1, updateUserDto);

                Assertions.assertNotNull(actual);
                Assertions.assertEquals(1, actual.id());
                Assertions.assertEquals(updateUserDto.firstName(), actual.firstName());
                Assertions.assertEquals(updateUserDto.lastName(), actual.lastName());
                Assertions.assertEquals(updateUserDto.phoneNumber(), actual.phoneNumber());
        }

        @Test
        void shouldDeleteUserAndRelateEntityIfSuccess() {

                var mockUser = new User(1, "john",
                                "doe", "0000000000");

                when(userRepository.findById(anyInt())).thenReturn(Optional.of(mockUser));

                userService.deleteUserById(1);

                verify(userPointService, times(1))
                                .deleteUserPointByUserId(mockUser.id());
                verify(userWalletService, times(1))
                                .deleteUserWalletByUserId(mockUser.id());
                verify(authService, times(1))
                                .deleteUserCredentialByUserId(mockUser.id());
                verify(userRepository, times(1)).delete(mockUser);

        }

}