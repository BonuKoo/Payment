package com.wallet.ledger.ledger.adapter.out.persistence.repository;

import com.wallet.ledger.ledger.adapter.out.persistence.entity.LedgerTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaLedgerTransactionRepository extends JpaRepository<LedgerTransaction,Long> {
    boolean existsByOrderId(String orderId);
}
