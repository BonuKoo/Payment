package com.wallet.ledger.ledger.adapter.out.persistence.entity;

import com.wallet.ledger.common.IdempotencyCreator;
import com.wallet.ledger.ledger.domain.LedgerTransactionDTO;
import org.springframework.stereotype.Component;

@Component
public class JpaLedgerTransactionMapper {

  public LedgerTransaction mapToJpaEntity (LedgerTransactionDTO ledgerTransactionDTO){

      /* param 순서
      1. descrip 2. referType 3. orderId 4. idempoKey 5. referId
      */
      return new LedgerTransaction(
        "LedgerService record transaction",
              ledgerTransactionDTO.getReferenceType().name(),
              ledgerTransactionDTO.getOrderId(),
              IdempotencyCreator.create(ledgerTransactionDTO),
              ledgerTransactionDTO.getReferenceId()
      );
  }

}
