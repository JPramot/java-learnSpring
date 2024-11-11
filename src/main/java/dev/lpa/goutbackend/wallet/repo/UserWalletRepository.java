package dev.lpa.goutbackend.wallet.repo;

import org.springframework.data.repository.ListCrudRepository;

import dev.lpa.goutbackend.wallet.models.UserWallet;

public interface UserWalletRepository extends ListCrudRepository<UserWallet, Integer> {

}
