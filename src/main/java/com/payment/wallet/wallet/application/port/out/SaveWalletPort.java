package com.payment.wallet.wallet.application.port.out;

import com.payment.wallet.wallet.domain.WalletDTO;

import java.util.List;

public interface SaveWalletPort {

    void save(List<WalletDTO> wallets);

}
