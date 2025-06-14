package com.wallet.ledger.ledger.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "payment_orders")
public class PaymentOrder {

    @Id
    private Long id;

    private BigDecimal amount;

    @Column(name = "order_id")
    private String orderId;

    public PaymentOrder() {
    }

    public PaymentOrder(Long id, BigDecimal amount, String orderId) {
        this.id = id;
        this.amount = amount;
        this.orderId = orderId;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getOrderId() {
        return orderId;
    }
}
