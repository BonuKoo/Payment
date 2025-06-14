package com.payment.payment.adapter.out.persistent.repository.springdata;

import com.payment.domain.payment.PaymentOrder;
import com.payment.payment.adapter.out.persistent.repository.querydsl.PaymentOrderRepository4Query;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaPaymentOrderRepository extends JpaRepository<PaymentOrder, Long>, PaymentOrderRepository4Query {

    Optional<PaymentOrder> findByOrderId(String orderId);

}
