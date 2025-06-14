package com.payment.wallet.wallet.domain;

public enum WalletEventMessageType {
    SUCCESS("정상 성공")
    ;

    private final String description;

    WalletEventMessageType(String description) {
    this.description = description;
    }
}
