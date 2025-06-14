package com.payment.payment.application.service;

import com.payment.payment.application.port.in.PaymentCompleteUseCase;
import com.payment.payment.application.port.out.CompletePaymentPort;
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
    private final CompletePaymentPort completePaymentPort;

    @Override
    public void completePaymentWallet(WalletEventMessage walletEventMessage) {
        PaymentEventDto paymentEventAndOrders = loadPaymentPort.getPaymentEventAndOrders(walletEventMessage.getOrderId());
        // paymentEventAndOrders에서 id를 확인 후 confirmWalletUpdate
        paymentEventAndOrders.confirmWalletUpdate();
        // completeIfDone
        paymentEventAndOrders.completeIfDone();
        completePaymentPort.complete(paymentEventAndOrders);
    }

    @Override
    public void completePaymentLedger(LedgerEventMessage ledgerEventMessage) {
        PaymentEventDto paymentEventAndOrders = loadPaymentPort.getPaymentEventAndOrders(ledgerEventMessage.getOrderId());
        paymentEventAndOrders.confirmLedgerUpdate();
        paymentEventAndOrders.completeIfDone();
        completePaymentPort.complete(paymentEventAndOrders);
    }
}
