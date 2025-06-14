package com.payment.payment.adapter.out.persistent.repository.querydsl;

import com.payment.domain.payment.PaymentEventMessage;

import java.util.List;

public interface PaymentOutboxRepository4Query {

    void markMessageAsSent(String orderId, String type);
    void markMessageAsFailure(String orderId, String type);
    List<PaymentEventMessage> findPendingPaymentOutboxes();

}
