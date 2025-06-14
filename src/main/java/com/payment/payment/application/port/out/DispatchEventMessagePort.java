package com.payment.payment.application.port.out;

import com.payment.domain.payment.PaymentEventMessage;

public interface DispatchEventMessagePort {

    void dispatch(PaymentEventMessage message);

}
