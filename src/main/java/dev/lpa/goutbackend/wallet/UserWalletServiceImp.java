package dev.lpa.goutbackend.wallet;

import java.math.BigDecimal;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Service;

import dev.lpa.goutbackend.commons.exceptions.EntityNotFound;
import dev.lpa.goutbackend.user.dtos.CreateUserWalletDto;
import dev.lpa.goutbackend.wallet.models.UserWallet;
import dev.lpa.goutbackend.wallet.repo.UserWalletRepository;

@Service
public class UserWalletServiceImp implements UserWalletService {

    private final UserWalletRepository userWalletRepository;
    private final Logger logger = LoggerFactory.getLogger(UserWalletServiceImp.class);

    public UserWalletServiceImp(UserWalletRepository userWalletRepository) {
        this.userWalletRepository = userWalletRepository;
    }

    @Override
    public UserWallet createUserWallet(CreateUserWalletDto userWalletDto) {
        BigDecimal balance = new BigDecimal(0);
        var userWallet = new UserWallet(null,
                AggregateReference.to(userWalletDto.userId()),
                userWalletDto.lastUpdated(),
                balance);
        var savedUserWallet = userWalletRepository.save(userWallet);
        logger.debug("create userWallet with userId: {}", userWalletDto.userId());
        return savedUserWallet;
    }

    @Override
    public void deleteUserWalletByUserId(int userId) {

        var existUserWallet = findUserWalletByUserId(userId)
                .orElseThrow(() -> new EntityNotFound(String.format("userWallet with userId: %d not found", userId)));

        userWalletRepository.delete(existUserWallet);
        logger.debug("delete userWallet with userId: {}", userId);
    }

    @Override
    public Optional<UserWallet> findUserWalletByUserId(int userId) {

        return userWalletRepository.findByUserId(AggregateReference.to(userId));
    }

}
