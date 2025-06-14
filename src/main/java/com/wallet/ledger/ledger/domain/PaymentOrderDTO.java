package com.wallet.ledger.ledger.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class PaymentOrderDTO extends ItemDTO{

    @Builder
    public PaymentOrderDTO(Long id, Long amount, String orderId) {
        super(id, amount, orderId, ReferenceType.PAYMENT_ORDER);
    }
}
