package com.payment.wallet.wallet.adapter.in.stream;

import com.payment.wallet.wallet.application.port.in.SettlementUseCase;
import com.payment.wallet.wallet.domain.PaymentEventMessage;
import com.payment.wallet.wallet.domain.WalletEventMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class PaymentEventMessageHandler {

    private final SettlementUseCase settlementUseCase;
    private final StreamBridge streamBridge;

    @Bean
    public Consumer<Message<PaymentEventMessage>> consume() {
        return paymentEventMessage ->
        {
            WalletEventMessage walletEventMessage = settlementUseCase.processSettlement(paymentEventMessage.getPayload());
            streamBridge.send("wallet",walletEventMessage);
        };
    }

}

/**
 * 1. 카프카에서 갖고 온 메시지를 SettlementUseCase에게 전달
 */
/**
 *   2. stream Bridge를 사용해서, walletEventMessage를 카프카 토픽으로 발행
 */