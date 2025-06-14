package com.wallet.ledger.ledger.application.port.out;

import com.wallet.ledger.ledger.adapter.out.persistence.entity.PaymentOrder;
import com.wallet.ledger.ledger.domain.PaymentOrderDTO;

import java.util.List;

public interface LoadPaymentOrderPort {

    List<PaymentOrderDTO> getPaymentOrders(String orderId);

}
