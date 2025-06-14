package com.payment.payment.adapter.in.stream;

import com.payment.payment.application.port.in.PaymentCompleteUseCase;
import com.payment.payment.domain.LedgerEventMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component

@RequiredArgsConstructor
public class LedgerEventMessageHandler {

    private final PaymentCompleteUseCase paymentCompleteUseCase;

    @KafkaListener(topics = "ledger-topic", groupId = "payment-service")
    public void ledger (@Payload LedgerEventMessage message,
                        @Header(KafkaHeaders.ACKNOWLEDGMENT) Acknowledgment acknowledgment){

        try {

            paymentCompleteUseCase.completePaymentLedger(message);

            acknowledgment.acknowledge();

        } catch (Exception e){

            // todo 예외 처리 , 로깅, 재시도
            // acknowledgment.acknowledge(); 생략 시 메시지 다시 소비 가능
            throw e; // 혹은 적절히 처리

        }

    }
}
