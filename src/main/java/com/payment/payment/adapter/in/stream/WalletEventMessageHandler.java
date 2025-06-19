package com.payment.payment.adapter.in.stream;

import com.payment.payment.application.port.in.PaymentCompleteUseCase;
import com.payment.payment.domain.WalletEventMessage;
import org.springframework.context.annotation.Bean;

import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
//@Configuration
public class WalletEventMessageHandler {

    private final PaymentCompleteUseCase paymentCompleteUseCase;

    public WalletEventMessageHandler(PaymentCompleteUseCase paymentCompleteUseCase) {
        this.paymentCompleteUseCase = paymentCompleteUseCase;
    }

    @Bean
    public Consumer<WalletEventMessage> wallet(){
        return message -> {
            try {
                paymentCompleteUseCase.completePaymentWallet(message);
            } catch (Exception e){
                throw new RuntimeException("Wallet 처리 실패", e);
            }
        };
    }

    /*
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, WalletEventMessage> kafkaListenerContainerFactory(
            ConsumerFactory<String, WalletEventMessage> consumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, WalletEventMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL); // 수동 커밋
        return factory;
    }*/
}
