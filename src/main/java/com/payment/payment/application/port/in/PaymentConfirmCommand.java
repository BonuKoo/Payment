package com.payment.payment.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentConfirmCommand {

    private String paymentKey;
    private String orderId; // orderId
    private Long amount;
}
