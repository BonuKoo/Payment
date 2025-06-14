package com.payment.payment.application.service;

import com.payment.domain.payment.PaymentEventMessage;
import com.payment.payment.adapter.out.persistent.repository.springdata.SpringDataJpaPaymentOutboxRepository;
import com.payment.payment.application.port.in.PaymentEventMessageRelayUseCase;
import com.payment.payment.application.port.out.DispatchEventMessagePort;
import com.payment.util.JsonLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class PaymentEventMessageRelayService implements PaymentEventMessageRelayUseCase {

    private final SpringDataJpaPaymentOutboxRepository springDataJpaPaymentOutboxRepository;
    private final DispatchEventMessagePort dispatchEventMessagePort;
    private final JsonLogger jsonLogger;


    @Override
    @Async
    @Scheduled(fixedDelay = 1, initialDelay = 1, timeUnit = TimeUnit.SECONDS)
    public void relay() {
        List<PaymentEventMessage> messages = springDataJpaPaymentOutboxRepository.findPendingPaymentOutboxes();

        for (PaymentEventMessage message : messages){
            try {
                dispatchEventMessagePort.dispatch(message);
            }catch (Exception e){
                jsonLogger.error("Failed to relay message: {}", e.getMessage(), e);
            }
        }
    }
}

