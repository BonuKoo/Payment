package com.payment.wallet.wallet.application.port.out;

import com.payment.wallet.wallet.domain.WalletDTO;
import com.payment.wallet.wallet.domain.entity.Wallet;

import java.util.Set;

public interface LoadWalletPort {

    Set<WalletDTO> getWallets(Set<Long> sellerIds);

}
