package com.payment.payment.adapter.out.persistent.repository;

import com.payment.payment.application.port.out.PaymentStatusUpdateCommand;

public interface PaymentStatusUpdateRepository {
    Boolean updatePaymentStatusToExecuting(String orderId, String paymentKey);
    Boolean updatePaymentStatus(PaymentStatusUpdateCommand command);
}
