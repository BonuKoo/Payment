package com.payment.payment.application.port.in;

import com.payment.payment.domain.PaymentConfirmationResult;

public interface PaymentConfirmUseCase {

    PaymentConfirmationResult confirm(PaymentConfirmCommand command);

}
