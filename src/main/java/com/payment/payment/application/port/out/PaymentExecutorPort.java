package com.payment.payment.application.port.out;

import com.payment.payment.application.port.in.PaymentConfirmCommand;
import com.payment.payment.domain.PaymentExecutionResult;

public interface PaymentExecutorPort {

    PaymentExecutionResult execute(PaymentConfirmCommand command);
}
