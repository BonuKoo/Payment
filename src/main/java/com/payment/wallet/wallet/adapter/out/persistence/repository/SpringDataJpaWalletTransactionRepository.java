package com.payment.wallet.wallet.adapter.out.persistence.repository;

import com.payment.wallet.wallet.domain.entity.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaWalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {

    Boolean existsByOrderId(String orderId);

}
