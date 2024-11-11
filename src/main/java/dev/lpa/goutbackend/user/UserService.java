package dev.lpa.goutbackend.user;

import dev.lpa.goutbackend.user.dtos.CreateUserDto;
import dev.lpa.goutbackend.user.dtos.UpdateUserDto;
import dev.lpa.goutbackend.user.models.User;

public interface UserService {
    User createUser(CreateUserDto createUserDto);

    User findUserById(Integer id);

    User updateUserById(Integer id, UpdateUserDto updateUserById);

    void deleteUserById(Integer id);
} 
