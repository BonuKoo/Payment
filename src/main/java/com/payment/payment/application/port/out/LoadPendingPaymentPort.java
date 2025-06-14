package com.payment.payment.application.port.out;

import com.payment.payment.domain.PendingPaymentEvent;

import java.util.List;

public interface LoadPendingPaymentPort {

    List<PendingPaymentEvent> getPendingPayments();

}
