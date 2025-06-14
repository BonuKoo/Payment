package com.payment.payment.adapter.out.persistent.repository.springdata;

import com.payment.domain.payment.PaymentOrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaPaymentOrderHistoryRepository extends JpaRepository<PaymentOrderHistory, Long> {
}
