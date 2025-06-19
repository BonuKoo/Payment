package com.payment.wallet.wallet.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class PaymentOrderDTO extends Item{

    private Long id;
    private Long sellerId;
    private int amount;
    private String orderId;

    public PaymentOrderDTO(int amount, String orderId, Long referenceId, ReferenceType referenceType, Long id, Long sellerId) {
        super(amount, orderId, referenceId, referenceType);
        this.id = id;
        this.sellerId = sellerId;
    }
}
