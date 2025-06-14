package com.payment.payment.adapter.out.persistent.repository;

public interface PaymentValidationRepository {

    boolean isValid(String orderId, long amount);

}
