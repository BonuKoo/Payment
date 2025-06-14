package com.payment.wallet.wallet.adapter.out.persistence.repository;

import com.payment.wallet.wallet.domain.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface SpringDataJpaWalletRepository extends JpaRepository<Wallet,Long> {

    List<Wallet> findByUserIdIn(Set<Long> userIds);
    List<Wallet> findByIdIn(Set<Long> ids);
}
