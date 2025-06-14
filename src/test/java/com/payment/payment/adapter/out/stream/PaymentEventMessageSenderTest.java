package com.payment.payment.adapter.out.stream;

import com.payment.domain.payment.PaymentEventMessage;
import com.payment.domain.payment.PaymentEventMessageType;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Tag("ExternalIntegration")
class PaymentEventMessageSenderTest {

    @Autowired
    private PaymentEventMessageSender paymentEventMessageSender;

    @Test
    void shouldSendEventMessagesUsingPartitionKey() throws InterruptedException {
        List<PaymentEventMessage> messages = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("orderId", UUID.randomUUID().toString());

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("partitionKey", i);

            PaymentEventMessage message = new PaymentEventMessage(
                    PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS,
                    payload,
                    metadata
            );

            messages.add(message);
        }

        for (PaymentEventMessage message : messages) {
            paymentEventMessageSender.dispatch(message); // Kafka 전송
        }

        // Kafka 전송 결과 로그를 확인하기 위한 대기 시간
        Thread.sleep(5000);
    }
}
