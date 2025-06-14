package com.payment.wallet.wallet.adapter.out.persistence.repository;

import com.payment.wallet.wallet.domain.PaymentEventMessage;
import com.payment.wallet.wallet.domain.entity.WalletTransaction;

import java.util.List;

public interface WalletTransactionRepository  {

    Boolean isExist(PaymentEventMessage paymentEventMessage);

    void save(List<WalletTransaction> walletTransaction);
}
