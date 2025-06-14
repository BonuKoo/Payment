package com.payment.wallet.wallet.domain;

public enum PaymentEventMessageType {

    PAYMENT_CONFIRMATION_SUCCESS("결제 승인 완료 이벤트");

    private final String description;

    PaymentEventMessageType(String description) {
        this.description = description;
    }
}
