package com.payment.payment.adapter.out.persistent.repository;

import com.payment.domain.payment.PaymentOrder;

import java.util.List;
import java.util.Optional;

public interface PaymentOrderRepository {

    Optional<PaymentOrder> findByIdempotencyKey(String orderId);

    List<PaymentOrder> findListPaymentOrderByIdempotencyKey(String orderId);

    boolean isValid(String orderId, long amount);

    long incrementFailedCountByOrderId(String orderId);

    void saveAll(List<PaymentOrder> orders);

}
