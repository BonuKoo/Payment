package com.payment.wallet.wallet.adapter.out.persistence;

import com.payment.wallet.wallet.adapter.out.persistence.repository.PaymentOrderRepository;
import com.payment.wallet.wallet.application.port.out.LoadPaymentOrderPort;
import com.payment.wallet.wallet.domain.PaymentOrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentOrderPersistenceAdapter implements LoadPaymentOrderPort {

    private final PaymentOrderRepository paymentOrderRepository;


    @Override
    public List<PaymentOrderDTO> getPaymentOrders(String orderId) {
        return paymentOrderRepository.getPaymentOrders(orderId);
    }
}
