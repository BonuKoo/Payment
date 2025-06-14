package com.payment.wallet.wallet.adapter.out.persistence.repository;

import com.payment.wallet.wallet.domain.PaymentOrderDTO;
import com.payment.wallet.wallet.domain.entity.JpaPaymentOrderMapper;
import com.payment.wallet.wallet.domain.entity.PaymentOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaPaymentOrderRepository implements PaymentOrderRepository{

   private final SpringDataJpaPaymentOrderRepository springDataJpaPaymentOrderRepository;
   private final JpaPaymentOrderMapper jpaPaymentOrderMapper;

    @Override
    public List<PaymentOrderDTO> getPaymentOrders(String orderId) {
        return springDataJpaPaymentOrderRepository.findByOrderId(orderId).stream()
                .map(jpaPaymentOrderMapper::mapToDomainEntity)
                .toList();
    }
}
