package com.payment.payment.domain;

import com.payment.domain.payment.PaymentEventMessage;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventMessagePublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public PaymentEventMessagePublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
    public void publish(PaymentEventMessage message){
        applicationEventPublisher.publishEvent(message);
    }

}
/**
 * 어플리케이션 내부에서 도메인 이벤트를 발행하는 역할을 한다.
 * Spring 내부 컴포넌트 간 메시지 ( 이벤트 ) 전달
 * PaymentEventMessage는 Spring의 이벤트 리스너에서 받을 수 있는 이벤트 객체로서 활용된다.
 *  -> @TransactionalEventListener(phase = AFTER_COMMIT)를 통해, DB 트랜잭션 커밋 아후
 *  이벤트를 발행하도록 설정해야 한다.
 */


/**
 *
 * 결제 상태가 성공으로 변경되고, 메시지가 DB에 저장되었다면 해당 이벤트를 Kafka Topic에 즉시 발행되도록
 * 만든다.
 * 이렇게 함으로써 (성공한다는 가정 하에) 메시지 전송은 대부분 즉시 전송될 것이므로
 * 대략의 이벤트 발생 메시지를, DB에서 읽어올 필요가 없게 된다.
 *
 * 이 EventPublisher는, 트랜잭션이 DB에 성공적으로 커밋된 후에 이벤트를 발생하도록
 * 만들기 위해서 트랜잭셔널 이벤트 퍼블리셔를 사용
 *
 * 함수를 통해서, 트랜잭션이 성공적으로 커밋될 때까지 지연시킨다.
 * 그러니까 트랜잭션이 롤백되면 이벤트는 발생하지 않는다.
 *
 */
