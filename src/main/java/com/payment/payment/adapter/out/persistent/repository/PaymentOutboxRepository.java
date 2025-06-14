package com.payment.payment.adapter.out.persistent.repository;

import com.payment.domain.outbox.Outbox;
import com.payment.domain.payment.PaymentEventMessage;

import java.util.List;
import java.util.Optional;

public interface PaymentOutboxRepository {

    Optional<Outbox> findByIdempotencyKey(String key);
    void markMessageAsSent(String orderId, String type);
    void markMessageAsFailure(String orderId, String type);
    List<PaymentEventMessage> findPendingPaymentOutboxes();
}
