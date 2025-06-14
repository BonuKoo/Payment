package com.payment.payment.application.service;

import com.payment.payment.application.port.in.PaymentCompleteUseCase;
import com.payment.payment.application.port.out.LoadPaymentPort;
import com.payment.payment.domain.LedgerEventMessage;
import com.payment.payment.domain.PaymentEventDto;
import com.payment.payment.domain.WalletEventMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentCompleteService implements PaymentCompleteUseCase {

    private final LoadPaymentPort loadPaymentPort;

    @Override
    public void completePaymentWallet(WalletEventMessage walletEventMessage) {
        PaymentEventDto paymentEventAndOrders = loadPaymentPort.getPaymentEventAndOrders(walletEventMessage.getOrderId());
        paymentEventAndOrders.

    }

    @Override
    public void completePaymentLedger(LedgerEventMessage ledgerEventMessage) {
        loadPaymentPort.getPaymentEventAndOrders(ledgerEventMessage.getOrderId());
    }
}
