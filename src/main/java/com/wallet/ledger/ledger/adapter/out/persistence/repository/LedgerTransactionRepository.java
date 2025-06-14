package com.wallet.ledger.ledger.adapter.out.persistence.repository;

import com.wallet.ledger.ledger.domain.PaymentEventMessage;

public interface LedgerTransactionRepository {

    Boolean isExist(PaymentEventMessage message);

}
