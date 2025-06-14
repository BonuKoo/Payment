package com.payment.payment.application.port.in;

import com.payment.payment.domain.LedgerEventMessage;
import com.payment.payment.domain.WalletEventMessage;

public interface PaymentCompleteUseCase {

    void completePaymentWallet(WalletEventMessage walletEventMessage);

    void completePaymentLedger(LedgerEventMessage ledgerEventMessage);

}
