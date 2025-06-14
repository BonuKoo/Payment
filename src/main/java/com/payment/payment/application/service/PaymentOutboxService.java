package com.payment.payment.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.domain.outbox.Outbox;
import com.payment.domain.outbox.OutboxStatus;
import com.payment.domain.payment.PaymentEventMessage;
import com.payment.domain.payment.PaymentEventMessageType;
import com.payment.domain.payment.PaymentStatus;
import com.payment.payment.adapter.out.persistent.repository.springdata.SpringDataJpaPaymentOutboxRepository;
import com.payment.payment.adapter.out.stream.util.PartitionKeyUtil;
import com.payment.payment.application.port.out.PaymentStatusUpdateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentOutboxService {

    private final ObjectMapper objectMapper;
    private final SpringDataJpaPaymentOutboxRepository outboxRepository;
    private final PartitionKeyUtil partitionKeyUtil;

    public PaymentEventMessage insertOutbox(PaymentStatusUpdateCommand command) {

        if (command.getStatus() != PaymentStatus.SUCCESS) {
            throw new IllegalArgumentException("Only SUCCESS status is allowed");
        }

        PaymentEventMessage message = createPaymentEventMessage(command);

        String payloadJson = toJson(message.getPayload());
        String metadataJson = toJson(message.getMetadata());

        Outbox outbox = Outbox.builder()
                .idempotencyKey((String) message.getPayload().get("orderId"))
                .type(message.getMessageType().name())
                .status(OutboxStatus.INIT)
                .partitionKey((Integer) message.getMetadata().get("partitionKey"))
                .payload(payloadJson)
                .metadata(metadataJson)
                .build();

        outboxRepository.save(outbox);

        return message;

    }

    private PaymentEventMessage createPaymentEventMessage(PaymentStatusUpdateCommand command) {
        int partitionKey = partitionKeyUtil.createPartitionKey(command.getOrderId().hashCode());

        Map<String, Object> payload = Map.of("orderId", command.getOrderId());
        Map<String, Object> metadata = Map.of("partitionKey", partitionKey);

        return new PaymentEventMessage(PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS, payload, metadata);
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON", e);
        }
    }
}
