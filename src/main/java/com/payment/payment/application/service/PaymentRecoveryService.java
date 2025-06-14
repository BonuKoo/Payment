package com.payment.payment.application.service;

import com.payment.payment.application.port.out.LoadPendingPaymentPort;
import com.payment.payment.adapter.out.persistent.exception.PaymentValidationException;
import com.payment.payment.adapter.out.web.toss.executor.TossPaymentExecutor;
import com.payment.payment.application.port.in.PaymentConfirmCommand;
import com.payment.payment.application.port.in.PaymentRecoveryUseCase;
import com.payment.payment.application.port.out.PaymentStatusUpdatePort;
import com.payment.payment.application.port.out.PaymentValidationPort;
import com.payment.payment.domain.PaymentExecutionResult;
import com.payment.payment.application.port.out.PaymentStatusUpdateCommand;
import com.payment.payment.domain.PendingPaymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("dev")
@RequiredArgsConstructor
public class PaymentRecoveryService implements PaymentRecoveryUseCase {

    private final LoadPendingPaymentPort loadPendingPaymentPort;
    private final PaymentValidationPort paymentValidationPort;
    private final TossPaymentExecutor tossPaymentExecutor;
    private final PaymentStatusUpdatePort paymentStatusUpdatePort;
    private final PaymentErrorHandler paymentErrorHandler;

    @Override
    @Async
    @Scheduled(fixedDelay = 180_000, initialDelay = 180_000) // milliseconds
    public void recovery() {

        List<PendingPaymentEvent> pendingPayments = loadPendingPaymentPort.getPendingPayments();

        for (PendingPaymentEvent paymentEvent : pendingPayments){
            PaymentConfirmCommand command = PaymentConfirmCommand.builder()
                    .paymentKey(paymentEvent.getPaymentKey())
                    .orderId(paymentEvent.getOrderId())
                    .amount(paymentEvent.totalAmount())
                    .build();

            try {
                boolean isValid = paymentValidationPort.isValid(command.getOrderId(), command.getAmount());
                if (!isValid){
                    throw new PaymentValidationException("Validation failed for order: " + command.getOrderId());
                }
                PaymentExecutionResult result = tossPaymentExecutor.execute(command);
                paymentStatusUpdatePort.updatePaymentStatus(new PaymentStatusUpdateCommand(result));

            } catch (Exception e){
                paymentErrorHandler.handlePaymentConfirmationError(e, command);
            }

        }

    }

}
