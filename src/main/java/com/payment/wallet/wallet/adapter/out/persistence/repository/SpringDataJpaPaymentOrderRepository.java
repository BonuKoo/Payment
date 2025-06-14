package com.payment.wallet.wallet.adapter.out.persistence.repository;

import com.payment.wallet.wallet.domain.PaymentOrderDTO;
import com.payment.wallet.wallet.domain.entity.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataJpaPaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {

    List<PaymentOrder> findByOrderId(String orderId);

}
