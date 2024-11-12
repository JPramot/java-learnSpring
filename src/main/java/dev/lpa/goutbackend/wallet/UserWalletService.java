package dev.lpa.goutbackend.wallet;

import java.util.Optional;

import dev.lpa.goutbackend.user.dtos.CreateUserWalletDto;
import dev.lpa.goutbackend.wallet.models.UserWallet;

public interface UserWalletService {
    UserWallet createUserWallet(CreateUserWalletDto userWalletDto);

    Optional<UserWallet> findUserWalletByUserId(int userId);

    void deleteUserWalletByUserId(int userId);
}
