package dev.lpa.goutbackend.point;

import java.util.Optional;

import dev.lpa.goutbackend.point.models.UserPoint;
import dev.lpa.goutbackend.user.dtos.CreateUserPointDto;

public interface UserPointService {

    UserPoint createUserPoint(CreateUserPointDto userPointDto);

    Optional<UserPoint> findUserPointByUserId(int userId);

    void deleteUserPointByUserId(int userId);
}
