package com.payment.wallet.wallet.domain.entity;

import com.payment.wallet.wallet.domain.PaymentOrderDTO;
import org.springframework.stereotype.Component;

@Component
public class JpaPaymentOrderMapper {

    public PaymentOrderDTO mapToDomainEntity(PaymentOrder paymentOrder){
        return PaymentOrderDTO.builder()
                .id(paymentOrder.getId())
                .sellerId(paymentOrder.getSellerId())
                .amount(paymentOrder.getAmount().longValue())
                .orderId(paymentOrder.getOrderId())
                .build();
    }

}
