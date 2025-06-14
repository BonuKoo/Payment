package com.wallet.ledger.ledger.adapter.out.persistence.repository;

import com.wallet.ledger.ledger.adapter.out.persistence.entity.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaLedgerEntryRepository extends JpaRepository<LedgerEntry,Long> {
 }
