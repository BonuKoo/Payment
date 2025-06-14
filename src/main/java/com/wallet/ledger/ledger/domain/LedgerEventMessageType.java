package com.wallet.ledger.ledger.domain;

public enum LedgerEventMessageType {
    SUCCESS("장부 기입 성공");

    private final String description;

    LedgerEventMessageType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}