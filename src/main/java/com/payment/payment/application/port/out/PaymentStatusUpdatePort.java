package com.payment.payment.application.port.out;

public interface PaymentStatusUpdatePort {

    Boolean updatePaymentStatusToExecuting(String orderId, String paymentKey);
    Boolean updatePaymentStatus(PaymentStatusUpdateCommand command);
}
