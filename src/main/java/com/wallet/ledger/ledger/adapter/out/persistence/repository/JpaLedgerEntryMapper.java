package com.wallet.ledger.ledger.adapter.out.persistence.repository;

import com.wallet.ledger.ledger.adapter.out.persistence.entity.JpaLedgerTransactionMapper;
import com.wallet.ledger.ledger.adapter.out.persistence.entity.LedgerEntry;
import com.wallet.ledger.ledger.adapter.out.persistence.entity.LedgerTransaction;
import com.wallet.ledger.ledger.domain.DoubleLedgerEntry;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
public class JpaLedgerEntryMapper {

    private final JpaLedgerTransactionMapper jpaLedgerTransactionMapper;

    public JpaLedgerEntryMapper(JpaLedgerTransactionMapper jpaLedgerTransactionMapper) {
        this.jpaLedgerTransactionMapper = jpaLedgerTransactionMapper;
    }

    List<LedgerEntry> mapToJpaEntity(DoubleLedgerEntry doubleLedgerEntry){
        LedgerTransaction ledgerTransaction = jpaLedgerTransactionMapper.mapToJpaEntity(doubleLedgerEntry.getLedgerTransaction());

    /*
        1. amount
        2. accountId
        3. LedgerTransaction
        4. LedgerEntryType */
        LedgerEntry creditEntry =  LedgerEntry.builder()
                .amount(BigDecimal.valueOf(doubleLedgerEntry.getCredit().getAmount()))
                .accountId(doubleLedgerEntry.getCredit().getAccount().getId())
                .type(doubleLedgerEntry.getCredit().getType())
                .transaction(ledgerTransaction)
                .build();

        LedgerEntry debitEntry = LedgerEntry.builder()
                .amount(BigDecimal.valueOf(doubleLedgerEntry.getDebit().getAmount()))
                .accountId(doubleLedgerEntry.getDebit().getAccount().getId())
                .type(doubleLedgerEntry.getDebit().getType())
                .transaction(ledgerTransaction)
                .build();

        return Arrays.asList(creditEntry,debitEntry);
    }

}
