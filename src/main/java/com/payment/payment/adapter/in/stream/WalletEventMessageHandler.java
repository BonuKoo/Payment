package com.payment.payment.adapter.in.stream;

import com.payment.payment.application.port.in.PaymentCompleteUseCase;
import com.payment.payment.domain.WalletEventMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
//@Configuration
public class WalletEventMessageHandler {

    private final PaymentCompleteUseCase paymentCompleteUseCase;

    public WalletEventMessageHandler(PaymentCompleteUseCase paymentCompleteUseCase) {
        this.paymentCompleteUseCase = paymentCompleteUseCase;
    }



    @KafkaListener(
            topics = "wallet.events",
            groupId = "wallet-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void wallet (@Payload WalletEventMessage message,
                        @Header(KafkaHeaders.ACKNOWLEDGMENT) Acknowledgment acknowledgment){

     try {

         paymentCompleteUseCase.completePaymentWallet(message);

         acknowledgment.acknowledge();

     } catch (Exception e){

         // todo 예외 처리 , 로깅, 재시도
         // acknowledgment.acknowledge(); 생략 시 메시지 다시 소비 가능
         throw e; // 혹은 적절히 처리

     }

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
