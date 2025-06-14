package com.payment.payment.adapter.out.persistent.repository.jpa;

import com.payment.domain.payment.PaymentOrder;
import com.payment.payment.adapter.out.persistent.repository.PaymentOrderRepository;
import com.payment.payment.adapter.out.persistent.repository.springdata.SpringDataJpaPaymentOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaPaymentOrderRepository implements PaymentOrderRepository {

    private final SpringDataJpaPaymentOrderRepository springDataJpaPaymentOrderRepository;

    @Override
    public Optional<PaymentOrder> findByIdempotencyKey(String orderId) {
        return springDataJpaPaymentOrderRepository.findByIdempotencyKey(orderId);
    }

    @Override
    public List<PaymentOrder> findListPaymentOrderByIdempotencyKey(String orderId) {
        return springDataJpaPaymentOrderRepository.findListPaymentOrderByIdempotencyKey(orderId);
    }

    @Override
    public boolean isValid(String orderId, long amount) {
        return springDataJpaPaymentOrderRepository.isValid(orderId, amount);
    }

    @Override
    public long incrementFailedCountByOrderId(String orderId) {
        return springDataJpaPaymentOrderRepository.incrementFailedCountByOrderId(orderId);
    }

    @Override
    public void saveAll(List<PaymentOrder> orders) {
        springDataJpaPaymentOrderRepository.saveAll(orders);
    }
}
