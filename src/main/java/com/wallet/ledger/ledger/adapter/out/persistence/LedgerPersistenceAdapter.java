package com.wallet.ledger.ledger.adapter.out.persistence;

import com.wallet.ledger.ledger.adapter.out.persistence.repository.LedgerEntryRepository;
import com.wallet.ledger.ledger.adapter.out.persistence.repository.LedgerTransactionRepository;
import com.wallet.ledger.ledger.application.port.out.DuplicateMessageFilterPort;
import com.wallet.ledger.ledger.application.port.out.SaveDoubleLedgerEntryPort;
import com.wallet.ledger.ledger.domain.DoubleLedgerEntry;
import com.wallet.ledger.ledger.domain.PaymentEventMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LedgerPersistenceAdapter implements DuplicateMessageFilterPort, SaveDoubleLedgerEntryPort {

    private final LedgerTransactionRepository ledgerTransactionRepository;
    private final LedgerEntryRepository ledgerEntryRepository;

    @Override
    public Boolean isAlreadyProcess(PaymentEventMessage message) {
        return ledgerTransactionRepository.isExist(message);
    }

    @Override
    public void save(List<DoubleLedgerEntry> doubleLedgerEntries) {
        ledgerEntryRepository.save(doubleLedgerEntries);
    }
}
