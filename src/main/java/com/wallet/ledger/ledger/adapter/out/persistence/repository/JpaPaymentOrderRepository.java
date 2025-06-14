package com.wallet.ledger.ledger.adapter.out.persistence.repository;

import com.wallet.ledger.ledger.adapter.out.persistence.entity.JpaPaymentOrderMapper;
import com.wallet.ledger.ledger.domain.PaymentOrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaPaymentOrderRepository implements PaymentOrderRepository{

    private final SpringDataJpaPaymentOrderRepository springDataJpaPaymentOrderRepository;
    private final JpaPaymentOrderMapper jpaPaymentOrderMapper;

    @Override
    public List<PaymentOrderDTO> getPaymentOrderPayments(String orderId) {
        return springDataJpaPaymentOrderRepository.findByOrderId(orderId)
                .stream()
                .map(jpaPaymentOrderMapper::mapToPaymentOrderDTO)
                .toList();
    }
}
