package com.wallet.ledger.ledger.adapter.out.persistence.entity;

import com.wallet.ledger.ledger.domain.LedgerEntryType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
@Table(name = "ledger_entries")
@Getter
public class LedgerEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    @Column(name = "account_id")
    private Long accountId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private LedgerTransaction transaction;

    @Enumerated(EnumType.STRING)
    private LedgerEntryType type;

    public LedgerEntry() {
    }

    @Builder
    public LedgerEntry(BigDecimal amount, Long accountId, LedgerTransaction transaction, LedgerEntryType type) {
        this.amount = amount;
        this.accountId = accountId;
        this.transaction = transaction;
        this.type = type;
    }
}
