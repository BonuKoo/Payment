package com.wallet.ledger.ledger.application.port.out;

import com.wallet.ledger.ledger.domain.DoubleLedgerEntry;

import java.util.List;

public interface SaveDoubleLedgerEntryPort {

    void save (List<DoubleLedgerEntry> doubleLedgerEntries);

}
