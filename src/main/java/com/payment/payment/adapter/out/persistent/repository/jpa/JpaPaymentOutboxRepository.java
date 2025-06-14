package com.payment.payment.adapter.out.persistent.repository.jpa;

import com.payment.domain.outbox.Outbox;
import com.payment.domain.payment.PaymentEventMessage;
import com.payment.payment.adapter.out.persistent.repository.PaymentOutboxRepository;
import com.payment.payment.adapter.out.persistent.repository.springdata.SpringDataJpaPaymentOutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaPaymentOutboxRepository implements PaymentOutboxRepository {

    private final SpringDataJpaPaymentOutboxRepository springDataJpaPaymentOutboxRepository;

    @Override
    public Optional<Outbox> findByIdempotencyKey(String key) {
        return springDataJpaPaymentOutboxRepository.findByIdempotencyKey(key);
    }

    @Override
    public void markMessageAsSent(String orderId, String type) {
        springDataJpaPaymentOutboxRepository.markMessageAsSent(orderId,type);
    }

    @Override
    public void markMessageAsFailure(String orderId, String type) {
        springDataJpaPaymentOutboxRepository.markMessageAsFailure(orderId,type);
    }

    @Override
    public List<PaymentEventMessage> findPendingPaymentOutboxes() {
        return springDataJpaPaymentOutboxRepository.findPendingPaymentOutboxes();
    }
}
