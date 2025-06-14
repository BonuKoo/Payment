package com.payment.payment.adapter.out.stream;

import com.payment.payment.adapter.out.persistent.repository.springdata.SpringDataJpaPaymentOutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageStatusUpdater {

    private final SpringDataJpaPaymentOutboxRepository springDataJpaPaymentOutboxRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markMessageAsSent(String orderId, String messageType) {
        springDataJpaPaymentOutboxRepository.markMessageAsSent(orderId, messageType);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markMessageAsFailure(String orderId, String messageType) {
        springDataJpaPaymentOutboxRepository.markMessageAsFailure(orderId, messageType);
    }

}
