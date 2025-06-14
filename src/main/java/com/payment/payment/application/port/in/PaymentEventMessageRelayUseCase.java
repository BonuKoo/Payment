package com.payment.payment.application.port.in;

public interface PaymentEventMessageRelayUseCase {

    void relay();

}

/**
 * 아웃박스에 저장된 이벤트 메시지들을 갖고 와서 메시지 큐로 전송하는 기능을 담당

 * 아직 카프카 토픽으로 전송되지 않은 이벤트 메시지들을 갖고 와서 전송하도록 만든다.
 *
 *
 */