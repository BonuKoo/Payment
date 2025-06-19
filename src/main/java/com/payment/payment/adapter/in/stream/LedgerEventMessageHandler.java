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

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class LedgerEventMessageHandler {

    private final PaymentCompleteUseCase paymentCompleteUseCase;

    @Bean
    public Consumer<LedgerEventMessage> ledger(){
        return ledgerEventMessage -> {
            try {
                paymentCompleteUseCase.completePaymentLedger(ledgerEventMessage);
            } catch (Exception e){
                throw new RuntimeException("Ledger 처리 실패", e);
            }
        };
    }
}
