package com.payment.wallet.wallet.domain;

public enum TransactionType {
    CREDIT(""),
    DEBIT("");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }
}
