package com.wallet.ledger.ledger.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LedgerTransactionDTO {

    private ReferenceType referenceType;
    private Long referenceId;
    private String orderId;

    @Builder
    public LedgerTransactionDTO(ReferenceType referenceType, Long referenceId, String orderId) {
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.orderId = orderId;
    }
}
