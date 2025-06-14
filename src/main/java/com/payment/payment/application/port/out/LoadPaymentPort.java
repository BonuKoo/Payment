package com.payment.payment.application.port.out;

import com.payment.domain.payment.PaymentEvent;

public interface LoadPaymentPort {

    PaymentEvent getPayment(String orderId);

}
