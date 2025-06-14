package com.payment.payment.adapter.out.stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaMetadataHandler {

    @ServiceActivator(inputChannel = "payment-result")
    public void handleMetadata(Message<?> message) {
        log.info("Received Kafka send result metadata: {}", message);
        // 필요하면 여기서 전송 결과 분석 및 로깅 또는 DB 저장 등 처리 가능
    }

}
