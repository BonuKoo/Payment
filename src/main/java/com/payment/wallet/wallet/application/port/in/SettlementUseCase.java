package com.payment.wallet.wallet.application.port.in;

import com.payment.wallet.wallet.domain.PaymentEventMessage;
import com.payment.wallet.wallet.domain.WalletEventMessage;

public interface SettlementUseCase {

    WalletEventMessage processSettlement(PaymentEventMessage paymentEventMessage);

}
