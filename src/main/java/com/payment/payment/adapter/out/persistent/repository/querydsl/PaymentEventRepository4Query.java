package com.payment.payment.adapter.out.persistent.repository.querydsl;

import com.payment.payment.domain.PaymentEventDto;
import com.payment.payment.domain.PendingPaymentRowDto;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentEventRepository4Query {
    List<PendingPaymentRowDto> findPendingPaymentRows(LocalDateTime now);
    PaymentEventDto getPaymentEventAndOrders(String orderId);
}
