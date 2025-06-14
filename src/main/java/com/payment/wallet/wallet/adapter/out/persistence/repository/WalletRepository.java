package com.payment.wallet.wallet.adapter.out.persistence.repository;

import com.payment.wallet.wallet.domain.WalletDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface WalletRepository {

    Set<WalletDTO> getWallets(Set<Long> sellerIds);

    void save(List<WalletDTO> wallets);

    void update(List<WalletDTO> wallets);
}
