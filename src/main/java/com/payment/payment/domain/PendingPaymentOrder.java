package com.payment.payment.domain;

import com.payment.domain.payment.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class PendingPaymentOrder {

    private Long paymentOrderId;
    private PaymentStatus status;
    private Long amount;
    private int failedCount;
    private int threshold;

}
