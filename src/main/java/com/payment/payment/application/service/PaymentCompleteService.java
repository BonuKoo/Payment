package com.payment.payment.application.service;

import com.payment.payment.application.port.in.PaymentCompleteUseCase;
import com.payment.payment.domain.LedgerEventMessage;
import com.payment.payment.domain.WalletEventMessage;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class PaymentCompleteService implements PaymentCompleteUseCase {

    @Override
    public void completePaymentWallet(WalletEventMessage walletEventMessage) {
    }

    @Override
    public void completePaymentLedger(LedgerEventMessage ledgerEventMessage) {

    }
}
