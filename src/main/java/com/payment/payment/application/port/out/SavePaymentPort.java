package com.payment.payment.application.port.out;

import com.payment.domain.payment.PaymentEvent;

public interface SavePaymentPort {
    void save(PaymentEvent paymentEvent);
}
