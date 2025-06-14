package com.payment.payment.adapter.out.persistent.repository.querydsl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.domain.outbox.Outbox;
import com.payment.domain.outbox.OutboxStatus;
import com.payment.domain.outbox.QOutbox;
import com.payment.domain.payment.PaymentEventMessage;
import com.payment.domain.payment.PaymentEventMessageType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class PaymentOutboxRepository4QueryImpl implements PaymentOutboxRepository4Query {

    private final JPAQueryFactory queryFactory;
    private final ObjectMapper objectMapper;

    QOutbox outbox = new QOutbox(QOutbox.outbox);

    PaymentOutboxRepository4QueryImpl(EntityManager entityManager, ObjectMapper objectMapper) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public void markMessageAsSent(String orderId, String type) {

        queryFactory.update(outbox)
                .set(outbox.status, OutboxStatus.SUCCESS)
                .where(outbox.idempotencyKey.eq(orderId)
                        .and(outbox.type.eq(type)))
                .execute();

    }

    @Override
    @Transactional
    public void markMessageAsFailure(String orderId, String type) {
        queryFactory.update(outbox)
                .set(outbox.status, OutboxStatus.FAILURE)
                .where(outbox.idempotencyKey.eq(orderId)
                        .and(outbox.type.eq(type)))
                .execute();
    }

    @Override
    public List<PaymentEventMessage> findPendingPaymentOutboxes() {

        LocalDateTime now = LocalDateTime.now().minusMinutes(1);

        List<Outbox> outboxes = queryFactory
                .selectFrom(outbox)
                .where(outbox.status.in(OutboxStatus.INIT, OutboxStatus.FAILURE),
                        outbox.createdAt.loe(now),
                        outbox.type.eq(PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS.name()))
                .fetch();
        return
        outboxes.stream().map(o->{
            try {
                Map<String, Object> payload = objectMapper.readValue(o.getPayload(),Map.class);
                Map<String, Object> metadata = objectMapper.readValue(o.getMetadata(), Map.class);
                return new PaymentEventMessage(PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS, payload, metadata);
            } catch (Exception e){
                throw new RuntimeException("Failed to parse JSON", e);
            }
        }).toList();
    }

}
