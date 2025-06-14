package com.payment.payment.application.port.out;

import com.payment.domain.payment.PaymentEvent;
import com.payment.payment.domain.PaymentEventDto;

public interface CompletePaymentPort {

    void complete(PaymentEventDto paymentEvent);

}
