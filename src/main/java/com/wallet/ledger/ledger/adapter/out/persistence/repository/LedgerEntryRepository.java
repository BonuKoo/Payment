package com.wallet.ledger.ledger.adapter.out.persistence.repository;

import com.wallet.ledger.ledger.domain.DoubleLedgerEntry;

import java.util.List;

public interface LedgerEntryRepository {

    void save (List<DoubleLedgerEntry> doubleLedgerEntries);

}
