package com.wallet.ledger.ledger.adapter.out.persistence.repository;

import com.wallet.ledger.ledger.domain.PaymentEventMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaLedgerTransactionRepository implements LedgerTransactionRepository {

    private final SpringDataJpaLedgerTransactionRepository springDataJpaLedgerTransactionRepository;

    @Override
    public Boolean isExist(PaymentEventMessage message) {
       return springDataJpaLedgerTransactionRepository.existsByOrderId(message.getOrderId());
    }
}
