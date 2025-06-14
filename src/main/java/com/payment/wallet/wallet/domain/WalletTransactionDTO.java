package com.payment.wallet.wallet.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class WalletTransactionDTO {

    private Long id;
    private WalletDTO walletDTO;
    private BigDecimal amount;
    private TransactionType type;
    private ReferenceType referenceType;
    private Long referenceId;
    private String orderId;
    private String idempotencyKey;
}
