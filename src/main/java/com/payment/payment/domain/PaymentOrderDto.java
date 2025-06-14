package com.payment.payment.domain;

import com.payment.domain.payment.PaymentStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PaymentOrderDto {

    private Long id;
    private String sellerId;
    private String productId;
    private String orderId;
    private int amount;
    private PaymentStatus paymentStatus;
    private boolean isLedgerUpdated;
    private boolean isWalletUpdated;

    @QueryProjection
    public PaymentOrderDto(Long id, String sellerId, String productId, String orderId, PaymentStatus paymentStatus, int amount, boolean isLedgerUpdated, boolean isWalletUpdated) {
        this.id = id;
        this.sellerId = sellerId;
        this.productId = productId;
        this.orderId = orderId;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
        this.isLedgerUpdated = isLedgerUpdated;
        this.isWalletUpdated = isWalletUpdated;
    }

    // 메서드
    public boolean isLedgerUpdated() {
        return isLedgerUpdated;
    }

    public boolean isWalletUpdated() {
        return isWalletUpdated;
    }

    public void confirmWalletUpdate() {
        isWalletUpdated = true;
    }

    public void confirmLedgerUpdate() {
        isLedgerUpdated = true;
    }

}
