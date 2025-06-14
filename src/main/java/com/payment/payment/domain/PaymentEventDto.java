package com.payment.payment.domain;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PaymentEventDto {

    private Long id;
    private String orderId;
    private String orderName;
    private Long buyerId;
    private boolean isPaymentDone;
    private List<PaymentOrderDto> paymentOrders;

    @QueryProjection
    public PaymentEventDto(Long id, String orderId, String orderName, Long buyerId, boolean isPaymentDone, List<PaymentOrderDto> paymentOrders) {
        this.id = id;
        this.orderId = orderId;
        this.orderName = orderName;
        this.buyerId = buyerId;
        this.isPaymentDone = isPaymentDone;
        this.paymentOrders = paymentOrders;
    }
}
