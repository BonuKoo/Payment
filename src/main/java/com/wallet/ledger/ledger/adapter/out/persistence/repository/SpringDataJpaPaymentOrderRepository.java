package com.wallet.ledger.ledger.adapter.out.persistence.repository;

import com.wallet.ledger.ledger.adapter.out.persistence.entity.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataJpaPaymentOrderRepository extends JpaRepository<PaymentOrder,Long> {

    List<PaymentOrder> findByOrderId(String orderId);
}
