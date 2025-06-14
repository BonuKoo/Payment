package com.wallet.ledger.ledger.adapter.out.persistence.repository;


import com.wallet.ledger.ledger.domain.PaymentOrderDTO;

import java.util.List;

public interface PaymentOrderRepository {
   List<PaymentOrderDTO> getPaymentOrderPayments(String orderId);
}
