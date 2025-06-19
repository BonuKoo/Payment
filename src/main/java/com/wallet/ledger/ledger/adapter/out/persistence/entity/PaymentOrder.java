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
    @Column(name = "payment_order_id")
    private Long id;

    private int amount;

    @Column(name = "order_id")
    private String orderId;

    public PaymentOrder() {
    }

    public PaymentOrder(Long id, int amount, String orderId) {
        this.id = id;
        this.amount = amount;
        this.orderId = orderId;
    }

    public Long getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public String getOrderId() {
        return orderId;
    }
}
