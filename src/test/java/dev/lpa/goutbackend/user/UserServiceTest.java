// package dev.lpa.goutbackend.user;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyInt;
// import static org.mockito.Mockito.when;

// import java.math.BigDecimal;
// import java.time.Instant;
// import java.util.Optional;

// import org.junit.jupiter.api.Assertions;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.data.jdbc.core.mapping.AggregateReference;
// import org.springframework.security.crypto.password.PasswordEncoder;

// import dev.lpa.goutbackend.auth.models.UserLogin;
// import dev.lpa.goutbackend.auth.repo.UserLoginRepository;
// import dev.lpa.goutbackend.commons.exceptions.EntityNotFound;
// import dev.lpa.goutbackend.point.models.UserPoint;
// import dev.lpa.goutbackend.point.repo.UserPointRepository;
// import dev.lpa.goutbackend.user.dtos.CreateUserDto;
// import dev.lpa.goutbackend.user.dtos.UpdateUserDto;
// import dev.lpa.goutbackend.user.models.User;
// import dev.lpa.goutbackend.user.repo.UserRepository;
// import dev.lpa.goutbackend.wallet.models.UserWallet;
// import dev.lpa.goutbackend.wallet.repo.UserWalletRepository;

// @ExtendWith(MockitoExtension.class)
// class UserServiceTest {

// @InjectMocks
// private UserServiceImp userService;

// @Mock
// private UserRepository userRepository;

// @Mock
// private UserLoginRepository userLoginRepository;

// @Mock
// private UserPointRepository userPointRepository;

// @Mock
// private UserWalletRepository userWalletRepository;

// @Mock
// private PasswordEncoder passwordEncoder;

// @Test
// void shouldCreateUserWhenSuccess() {
// var mockUser = new User(1, "john", "doe", "1234567890");
// AggregateReference<User, Integer> userId =
// AggregateReference.to(mockUser.id());
// var now = Instant.now();
// var balance = new BigDecimal(0);
// var mockUserLogin = new UserLogin(1,
// userId,
// "john@gmail.com",
// "encodePassword");
// var mockUserWallet = new UserWallet(1, userId, now, balance);
// var mockUserPoint = new UserPoint(1, userId, now, 0);

// var createUserDto = new CreateUserDto("john",
// "doe",
// "1234567890",
// "john@gmail.com",
// "123456");

// when(userRepository.save(any(User.class))).thenReturn(mockUser);
// when(passwordEncoder.encode(createUserDto.password())).thenReturn("encodedPassword");
// when(userLoginRepository.save(any(UserLogin.class))).thenReturn(mockUserLogin);
// when(userPointRepository.save(any(UserPoint.class))).thenReturn(mockUserPoint);
// when(userWalletRepository.save(any(UserWallet.class))).thenReturn(mockUserWallet);

// var actual = userService.createUser(createUserDto);

// Assertions.assertNotNull(actual);
// Assertions.assertEquals(1, actual.id().intValue());
// Assertions.assertEquals(createUserDto.firstName(), actual.firstName());
// Assertions.assertEquals(createUserDto.lastName(), actual.lastName());
// Assertions.assertEquals(createUserDto.phoneNumber(), actual.phoneNumber());
// }

// @Test
// void shouldReturnUserWhenGetUserByIdSuccess() {
// var mockUser = new User(1, "john", "doe", "1234567890");
// when(userRepository.findById(anyInt())).thenReturn(Optional.of(mockUser));

// var actual = userService.findUserById(1);

// Assertions.assertNotNull(actual);
// Assertions.assertEquals(1, actual.id());
// Assertions.assertEquals(mockUser.firstName(), actual.firstName());
// Assertions.assertEquals(mockUser.lastName(), actual.lastName());
// Assertions.assertEquals(mockUser.phoneNumber(), actual.phoneNumber());

// }

// @Test
// void shouldThrowErrorWhenUserNotFound() {
// when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

// var exception = Assertions.assertThrows(EntityNotFound.class,
// () -> userService.findUserById(1));

// Assertions.assertEquals(String.format("user id: %d not found", 1),
// exception.getMessage());
// }

// @Test
// void shouldUpdateUserWhenSuccess() {
// var mockUser = new User(1, "john", "doe", "1234567890");

// when(userRepository.findById(anyInt())).thenReturn(Optional.of(mockUser));

// var newMock = new User(1, "jane", "doe", "1234567890");
// var updateUserDto = new UpdateUserDto("jane", "doe", "1234567890");

// when(userRepository.save(any(User.class))).thenReturn(newMock);

// var actual = userService.updateUserById(1, updateUserDto);

// Assertions.assertNotNull(actual);
// Assertions.assertEquals(1, actual.id().intValue());
// Assertions.assertEquals(updateUserDto.firstName(), actual.firstName());
// Assertions.assertEquals(updateUserDto.lastName(), actual.lastName());
// Assertions.assertEquals(updateUserDto.phoneNumber(), actual.phoneNumber());
// }

// @Test
// void shouldThrowErrorWhenUpdateUserByIdButUserNotfound() {
// when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

// var exception = Assertions.assertThrows(EntityNotFound.class,
// () -> userService.updateUserById(1, null));

// Assertions.assertEquals(String.format("user id: %d not found", 1),
// exception.getMessage());
// }

// @Test
// void shouldDeleteUserByIdWhenSuccess() {

// }

// }
