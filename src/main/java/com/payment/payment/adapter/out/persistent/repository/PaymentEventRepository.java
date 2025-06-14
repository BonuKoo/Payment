package com.payment.payment.adapter.out.persistent.repository;

import com.payment.domain.payment.PaymentEvent;
import com.payment.payment.domain.PaymentEventDto;
import com.payment.payment.domain.PendingPaymentEvent;
import com.payment.payment.domain.PendingPaymentRowDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentEventRepository {

    void save (PaymentEvent paymentEvent);

    Optional<PaymentEvent> findByOrderId(String orderId);

    List<PendingPaymentEvent> getPendingPayments();

    List<PendingPaymentRowDto> findPendingPaymentRows(LocalDateTime now);
    
    // 추후 orderId로 바꿔야 함
    PaymentEvent getPayment(String orderId);

    PaymentEventDto getPaymentEventAndOrders(String orderId);

    void complete(PaymentEventDto paymentEventDto);
}
