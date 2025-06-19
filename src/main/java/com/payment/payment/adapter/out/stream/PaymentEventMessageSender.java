package com.payment.payment.adapter.out.stream;

import com.payment.domain.payment.PaymentEventMessage;
import com.payment.payment.adapter.out.persistent.repository.springdata.SpringDataJpaPaymentOutboxRepository;
import com.payment.payment.application.port.out.DispatchEventMessagePort;
import com.payment.util.JsonLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.integration.IntegrationMessageHeaderAccessor;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentEventMessageSender implements DispatchEventMessagePort {

    private final StreamBridge streamBridge;
    private final SpringDataJpaPaymentOutboxRepository springDataJpaPaymentOutboxRepository;
    private final MessageStatusUpdater messageStatusUpdater;
    private final JsonLogger jsonLogger;

    private void send(PaymentEventMessage message) {

        Message<PaymentEventMessage> eventMessage = createEventMessage(message);

        boolean sent = streamBridge.send("send-out-0", eventMessage);

        log.info("Kafka send result = {}" ,sent);

        String orderId = (String) message.getPayload().get("orderId");
        String messageType = message.getMessageType().name();


        checkMarkMessage(message, sent, orderId, messageType);

    }

    private void checkMarkMessage(PaymentEventMessage message, boolean sent, String orderId, String messageType) {

        if(sent){
            messageStatusUpdater.markMessageAsSent(
                    orderId, messageType
            );

            jsonLogger.info(    "PaymentEventMessageSender",
                    "Successfully sent payment event message",
                    Map.of(
                            "orderId", orderId,
                            "messageType", messageType,
                            "payload", message.getPayload(),
                            "metadata", message.getMetadata()
                    )
            );

        } else{
            messageStatusUpdater.markMessageAsFailure(
                    orderId, messageType
            );

            jsonLogger.error(
                    "PaymentEventMessageSender",
                    "Failed to send payment event message",
                    new IllegalStateException("StreamBridge.send returned false")
            );

            // 실패 시 로깅 및 후처리
            throw new IllegalStateException("Failed to send payment event message");
        }
    }

    /**
     PaymentEventMessagePublisher에서 publish를 통해 발급받은 메시지를
     내부 이벤트 시스템을 이용해서,
     @TransactionalEventListener 가 붙은 메서드는, 이벤트 객체의 타입이 메서드 파라미터와 일치할 때 자동으로 호출된다.
     즉, PaymentEventMessage 타입을 publish 하면, 해당 타입을 파라미터로 받는 @TransactionalEventListener 메서드가 자동으로 실행된다.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void dispatchAfterCommit(PaymentEventMessage message) {
        dispatch(message);
    }

    public void dispatch(PaymentEventMessage message){
        send(message);
    }

    private Message<PaymentEventMessage> createEventMessage(PaymentEventMessage message){
        // orderId를 CORRELATION_ID로 설정
        Object orderId = message.getPayload().get("orderId");

        // partitionKey가 없는 경우 기본값 0을 설정
        Object partitionKey = message.getMetadata().getOrDefault("partitionKey", 0);

        jsonLogger.info(
                "PaymentEventMessageSender",
                "Creating event message with headers",
                Map.of(
                        "orderId", orderId,
                        "partitionKey", partitionKey
                )
        );

        return MessageBuilder.withPayload(message)
                /**
                 * Spring Integration에서 사용하는 Correlation ID(상관관계 ID).
                 * 요청-응답 패턴에서 트래킹을 위해 사용된다.
                 * 메시지의 흐름을 추적하거나, 이전 메시지와 연결고리를 만들기 위해서 설정

                 * -> 이 메시지가 어떤 주문(orderId)와 연관 있는가 에 대해 추적(트래킹)용
                 * 로깅,디버깅, 상태 확인 등에 사용
                 * */
                .setHeader(IntegrationMessageHeaderAccessor.CORRELATION_ID,orderId)
                /**
                 * Kafka 전송 시 메시지 키를 설정
                 * Kafka는 KEY 값을 기준으로, 어떤 파티션에 메시지를 보낼 지 결정
                 * 동일한 KEY 값을 가진 메시지는, 항상 같은 파티션에 들어간다.

                 * -> 파티셔닝 로직을 명확히 하고, 데이터 분산 제어 및 순서 보장
                 * */
//                .setHeader(KafkaHeaders.PARTITION, partitionKey)
                .build();
    }

    // Kafka로 보낸 메시지 전송 결과

}

/**
 * phase = AFTER_COMMIT
 * 이벤트 처리 중 예외가 발생해도 DB에 영향을 주지 않도록 트랜잭션 이후 실행
 * Kafka 발행 등의 외부 시스템과의 통신은 보통 트랜잭션 외부에서 처리하는 것이 안전
 */

/** 원본 코드
 외부 세상인 메시지 큐와 연결된다.
 Payment Event Message 산출에서 특정 타입의 데이터를 동적으로 발행하기 위해 Sinks를 사용.
 발행된 이벤트 페이지는 Kafka로 전송
 Sync는 리액티브 프로그래밍에서 데이터 스트림을 동적으로 생성하고 방출하는 메커니즘
 Sender는 Sinks에 데이터를 동적으로 넣어주면 카프카로 메시지를 발행하는

 */

/**

    원본 : Sinks.many() -> 메시지 큐
     Supplier<Flux<>> → Kafka로 데이터를 스트리밍하는 공급자
    Sinks는 Project Reactor 기반의 리액티브 프로그래밍 (WebFlux) 환경에서 쓰는 것
    -> Spring MVC 기반의 전통적 코드로 맞춰나가야 한다.

     @TransactionalEventListener + ApplicationEventPublisher 로 내부 이벤트 발행
     StreamBridge로 Kafka 등 외부 메시지 브로커 전송
 */

/**
 * Spring Integration에서 사용하는 Correlation ID(상관관계 ID).
 * 요청-응답 패턴에서 트래킹을 위해 사용된다.
 * 메시지의 흐름을 추적하거나, 이전 메시지와 연결고리를 만들기 위해서 설정
 *
 * -> 이 메시지가 어떤 주문(orderId)와 연관 있는가 에 대해 추적(트래킹)용
 * 로깅,디버깅, 상태 확인 등에 사용
 * */

/**
 * Sinks, Flux, Supplier는 MVC 환경에선 사용하지 않음
 * @Bean(name = "payment-result"), @ServiceActivator는 메시지 발신 결과를 Flux로 수신하지 않으므로 불필요
 */