package com.payment.wallet.wallet.adapter.out.persistence.repository;

import com.payment.wallet.wallet.domain.PaymentEventMessage;
import com.payment.wallet.wallet.domain.entity.WalletTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaWalletTransactionRepository implements WalletTransactionRepository{

    private final SpringDataJpaWalletTransactionRepository springDataJpaWalletTransactionRepository;

    @Override
    public Boolean isExist(PaymentEventMessage paymentEventMessage) {
        return springDataJpaWalletTransactionRepository.existsByOrderId(paymentEventMessage.getOrderId());

    }

    @Override
    public void save(List<WalletTransaction> walletTransaction) {
        springDataJpaWalletTransactionRepository.saveAll(walletTransaction);
    }
}
