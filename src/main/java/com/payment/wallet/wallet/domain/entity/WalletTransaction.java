package com.payment.wallet.wallet.domain.entity;

import com.payment.wallet.wallet.domain.ReferenceType;
import com.payment.wallet.wallet.domain.TransactionType;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wallet_transactions")
/*
        (name = "wallet_transactions", indexes = {
        @Index(name = "idx_wallet_id", columnList = "wallet_id"),
        @Index(name = "idx_idempotency_key", columnList = "idempotency_key", unique = true)
})*/
@NoArgsConstructor
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private ReferenceType referenceType;

    private Long referenceId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "idempotency_key")
    private String idempotencyKey;

    public WalletTransaction(String idempotencyKey, String orderId, Long referenceId, ReferenceType referenceType, TransactionType type, BigDecimal amount, Wallet wallet, Long id) {
        this.idempotencyKey = idempotencyKey;
        this.orderId = orderId;
        this.referenceId = referenceId;
        this.referenceType = referenceType;
        this.type = type;
        this.amount = amount;
        this.wallet = wallet;
        this.id = id;
    }

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /*
    @PrePersist
    public void prePersist() {
        this.createdAt = this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }*/

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
}

/**
    지갑에 대한 입출금 내역 관리
 */