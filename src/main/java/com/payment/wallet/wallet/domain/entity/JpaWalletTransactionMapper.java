package com.payment.wallet.wallet.domain.entity;

import com.payment.wallet.common.IdempotencyCreator;
import com.payment.wallet.wallet.domain.WalletDTO;
import com.payment.wallet.wallet.domain.WalletTransactionDTO;
import org.springframework.stereotype.Component;

@Component
public class JpaWalletTransactionMapper {

    public WalletTransaction mapToJpaEntity(WalletTransactionDTO walletTransactionDTO){
        return
        new WalletTransaction(
                IdempotencyCreator.create(walletTransactionDTO),
                        walletTransactionDTO.getOrderId(),
                        walletTransactionDTO.getReferenceId(),
                        walletTransactionDTO.getReferenceType(),
                        walletTransactionDTO.getType(),
                        walletTransactionDTO.getAmount(),
                mapToJpaEntity(walletTransactionDTO.getWalletDTO()),
                walletTransactionDTO.getId()
                );
    }

    private Wallet mapToJpaEntity(WalletDTO walletDTO){
        return new Wallet(walletDTO.getId(),
                walletDTO.getUserId(),
                walletDTO.getBalance(),
                walletDTO.getVersion()
        );

    }

}
