package dev.lpa.goutbackend.wallet.repo;

import org.springframework.data.repository.ListCrudRepository;

import dev.lpa.goutbackend.wallet.models.UserWallet;
import java.util.Optional;

import dev.lpa.goutbackend.user.models.User;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

public interface UserWalletRepository extends ListCrudRepository<UserWallet, Integer> {

    Optional<UserWallet> findByUserId(AggregateReference<User, Integer> userId);
}
