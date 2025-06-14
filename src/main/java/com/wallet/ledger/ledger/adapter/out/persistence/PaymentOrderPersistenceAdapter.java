package com.wallet.ledger.ledger.adapter.out.persistence;

import com.wallet.ledger.ledger.adapter.out.persistence.repository.PaymentOrderRepository;
import com.wallet.ledger.ledger.application.port.out.LoadPaymentOrderPort;
import com.wallet.ledger.ledger.domain.PaymentOrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentOrderPersistenceAdapter implements LoadPaymentOrderPort {

    private final PaymentOrderRepository paymentOrderRepository;

    @Override
    public List<PaymentOrderDTO> getPaymentOrders(String orderId) {
        return paymentOrderRepository.getPaymentOrderPayments(orderId);
    }
}
