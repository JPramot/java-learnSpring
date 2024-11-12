package dev.lpa.goutbackend.point;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Service;

import dev.lpa.goutbackend.commons.exceptions.EntityNotFound;
import dev.lpa.goutbackend.point.models.UserPoint;
import dev.lpa.goutbackend.point.repo.UserPointRepository;
import dev.lpa.goutbackend.user.dtos.CreateUserPointDto;

@Service
public class UserPointServiceImp implements UserPointService {

    private final UserPointRepository userPointRepository;
    private final Logger logger = LoggerFactory.getLogger(UserPointServiceImp.class);

    public UserPointServiceImp(UserPointRepository userPointRepository) {
        this.userPointRepository = userPointRepository;
    }

    @Override
    public UserPoint createUserPoint(CreateUserPointDto userPointDto) {
        var userPoint = new UserPoint(null,
                AggregateReference.to(userPointDto.userId()),
                userPointDto.lastUpdated(),
                0);
        var savedUserWallet = userPointRepository.save(userPoint);
        logger.debug("create userPoint with userId: {}", savedUserWallet.userId());
        return savedUserWallet;
    }

    @Override
    public void deleteUserPointByUserId(int userId) {
        var existUserPoint = findUserPointByUserId(userId)
                .orElseThrow(() -> new EntityNotFound(String.format("userPoint with userId: %d not found", userId)));

        userPointRepository.delete(existUserPoint);
        logger.debug("delete userPoint with userId: {}", userId);
    }

    @Override
    public Optional<UserPoint> findUserPointByUserId(int userId) {
        return userPointRepository.findByUserId(AggregateReference.to(userId));
    }

}
