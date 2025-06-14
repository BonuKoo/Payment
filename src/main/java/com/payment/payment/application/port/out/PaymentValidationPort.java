package com.payment.payment.application.port.out;

public interface PaymentValidationPort {

    Boolean isValid(String orderId, Long amount);

}
