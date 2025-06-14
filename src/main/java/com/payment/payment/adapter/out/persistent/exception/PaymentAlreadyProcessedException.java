package com.payment.payment.adapter.out.persistent.exception;

import com.payment.domain.payment.PaymentStatus;

public class PaymentAlreadyProcessedException extends RuntimeException {

  private final PaymentStatus status;

  public PaymentAlreadyProcessedException(String message, PaymentStatus status) {
        super(message);
        this.status = status;
    }
    public PaymentStatus getStatus(){
      return status;
    }
}
