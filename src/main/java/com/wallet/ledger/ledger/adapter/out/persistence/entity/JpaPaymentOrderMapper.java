package com.wallet.ledger.ledger.adapter.out.persistence.entity;

import com.wallet.ledger.ledger.domain.PaymentOrderDTO;
import org.springframework.stereotype.Component;

@Component
public class JpaPaymentOrderMapper {

    public PaymentOrderDTO mapToPaymentOrderDTO(PaymentOrder paymentOrder){
        return PaymentOrderDTO.builder()
                .id(paymentOrder.getId())
                .amount(paymentOrder.getAmount())
                .orderId(paymentOrder.getOrderId())
                .build();
    }

}
