package com.payment.wallet.wallet.adapter.out.persistence.repository;

import com.payment.wallet.wallet.domain.PaymentOrderDTO;
import com.payment.wallet.wallet.domain.entity.PaymentOrder;

import java.util.List;

public interface PaymentOrderRepository {

    List<PaymentOrderDTO> getPaymentOrders(String orderId);

}
