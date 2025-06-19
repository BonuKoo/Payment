package com.wallet.ledger.ledger.adapter.in.stream;

import com.wallet.ledger.ledger.application.port.in.DoubleLedgerEntryRecordUseCase;
import com.wallet.ledger.ledger.domain.LedgerEventMessage;
import com.wallet.ledger.ledger.domain.LedgerEventMessageType;
import com.wallet.ledger.ledger.domain.PaymentEventMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Configuration
@Component
@RequiredArgsConstructor
public class PaymentEventMessageHandler {

    private final DoubleLedgerEntryRecordUseCase doubleLedgerEntryRecordUseCase;
    private final StreamBridge streamBridge;

    @Bean
    public Consumer<Message<PaymentEventMessage>> consume() {
        return message -> {
            LedgerEventMessage ledgerEventMessage = doubleLedgerEntryRecordUseCase.recordDoubleLedgerEntry(message.getPayload());
            streamBridge.send("ledger", ledgerEventMessage);
        };
    }


}
