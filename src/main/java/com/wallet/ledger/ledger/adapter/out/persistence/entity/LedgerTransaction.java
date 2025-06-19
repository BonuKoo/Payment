package com.wallet.ledger.ledger.adapter.out.persistence.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ledger_transactions")
public class LedgerTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "reference_type")
    private String referenceType;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "idempotency_key")
    private String idempotencyKey;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public LedgerTransaction() {
    }

    public LedgerTransaction(Long id, String description, Long referenceId, String referenceType, String orderId, String idempotencyKey) {
        this.id = id;
        this.description = description;
        this.referenceId = referenceId;
        this.referenceType = referenceType;
        this.orderId = orderId;
        this.idempotencyKey = idempotencyKey;
    }

    public LedgerTransaction(String description, String referenceType, String orderId, String idempotencyKey, Long referenceId) {
        this.description = description;
        this.referenceType = referenceType;
        this.orderId = orderId;
        this.idempotencyKey = idempotencyKey;
        this.referenceId = referenceId;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }
}
