package com.payment.wallet.wallet.application.port.out;

import com.payment.wallet.wallet.domain.PaymentOrderDTO;

import java.util.List;

public interface LoadPaymentOrderPort {

    List<PaymentOrderDTO> getPaymentOrders(String orderId);

}

/**
    결제 주문을 갖고 오는 메서드
 */