package com.payment.wallet.wallet.adapter.out.persistence;

import com.payment.wallet.wallet.adapter.out.persistence.repository.WalletRepository;
import com.payment.wallet.wallet.adapter.out.persistence.repository.WalletTransactionRepository;
import com.payment.wallet.wallet.application.port.out.DuplicateMessageFilterPort;
import com.payment.wallet.wallet.application.port.out.LoadWalletPort;
import com.payment.wallet.wallet.application.port.out.SaveWalletPort;
import com.payment.wallet.wallet.application.port.out.UpdateWalletPort;
import com.payment.wallet.wallet.domain.PaymentEventMessage;
import com.payment.wallet.wallet.domain.WalletDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class WalletPersistenceAdapter implements DuplicateMessageFilterPort, LoadWalletPort, SaveWalletPort, UpdateWalletPort {

    private final WalletTransactionRepository walletTransactionRepository;
    private final WalletRepository walletRepository;

    @Override
    public Boolean isAlreadyProcess(PaymentEventMessage paymentEventMessage) {
        return walletTransactionRepository.isExist(paymentEventMessage);
    }

    @Override
    public Set<WalletDTO> getWallets(Set<Long> sellerIds) {
        return walletRepository.getWallets(sellerIds);
    }

    @Override
    public void save(List<WalletDTO> wallets) {
        walletRepository.save(wallets);
    }

    @Override
    public void update(List<WalletDTO> wallets) {
        walletRepository.update(wallets);
    }
}
