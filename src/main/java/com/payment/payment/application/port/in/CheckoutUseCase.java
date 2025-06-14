package com.payment.payment.application.port.in;

import com.payment.payment.domain.CheckoutResult;

public interface CheckoutUseCase {

    CheckoutResult checkout(CheckoutCommand command);

}
