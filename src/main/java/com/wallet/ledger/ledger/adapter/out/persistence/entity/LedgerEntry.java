package com.wallet.ledger.ledger.adapter.out.persistence.entity;

import com.wallet.ledger.ledger.domain.LedgerEntryType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

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
