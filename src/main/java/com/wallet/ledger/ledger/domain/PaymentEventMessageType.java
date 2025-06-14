package com.wallet.ledger.ledger.domain;

public enum PaymentEventMessageType {
    PAYMENT_CONFIRMATION_SUCCESS("결제 승인 성공");

    private final String description;

    PaymentEventMessageType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}