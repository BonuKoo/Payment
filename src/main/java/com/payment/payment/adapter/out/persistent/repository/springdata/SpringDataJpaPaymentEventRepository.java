package com.payment.payment.adapter.out.persistent.repository.springdata;

import com.payment.domain.payment.PaymentEvent;
import com.payment.payment.adapter.out.persistent.repository.querydsl.PaymentEventRepository4Query;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaPaymentEventRepository extends JpaRepository<PaymentEvent, Long>, PaymentEventRepository4Query {

    Optional<PaymentEvent> findByIdempotencyKey(String orderId);

    Optional<PaymentEvent> findByOrderName(String orderName);

//    Optional<PaymentEvent> findByOrderId(String orderId);

}
