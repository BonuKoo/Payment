package com.payment.payment.application.service;

import com.payment.payment.domain.PaymentFailure;
import com.payment.domain.payment.PaymentStatus;
import com.payment.payment.adapter.out.persistent.PaymentStatusUpdateRepository;
import com.payment.payment.adapter.out.persistent.exception.PSPConfirmationException;
import com.payment.payment.adapter.out.persistent.exception.PaymentAlreadyProcessedException;
import com.payment.payment.adapter.out.persistent.exception.PaymentValidationException;
import com.payment.payment.application.port.in.PaymentConfirmCommand;
import com.payment.payment.domain.PaymentConfirmationResult;
import com.payment.payment.application.port.out.PaymentStatusUpdateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
public class PaymentErrorHandler {

    private final PaymentStatusUpdateRepository paymentStatusUpdateRepository;

    public PaymentConfirmationResult handlePaymentConfirmationError(Throwable error, PaymentConfirmCommand command) {

        PaymentStatus status;
        PaymentFailure failure;

        if(error instanceof PSPConfirmationException e){
            status = e.paymentStatus();
            failure = new PaymentFailure(e.getErrorCode(), e.getMessage());
        } else if (error instanceof PaymentValidationException e) {
            status = PaymentStatus.FAILURE;
            failure = new PaymentFailure(e.getClass().getSimpleName(),e.getMessage());
        } else if (error instanceof PaymentAlreadyProcessedException e) {
            return new PaymentConfirmationResult(e.getStatus(),
                    new PaymentFailure(e.getClass().getSimpleName(), e.getMessage()));
        } else if (error instanceof TimeoutException e) {
            status = PaymentStatus.UNKNOWN;
            failure = new PaymentFailure(e.getClass().getSimpleName(), e.getMessage());
        } else {
            status = PaymentStatus.UNKNOWN;
            failure = new PaymentFailure(error.getClass().getSimpleName(), error.getMessage());
        }

        PaymentStatusUpdateCommand updateCommand = PaymentStatusUpdateCommand.builder()
                .paymentKey(command.getPaymentKey()).orderId(command.getOrderId())
                .status(status)
                .failure(failure)
                .build();

        paymentStatusUpdateRepository.updatePaymentStatus(updateCommand);
        return new PaymentConfirmationResult(status,failure);
    }

}
