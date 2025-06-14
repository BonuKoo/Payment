package com.wallet.ledger.ledger.adapter.out.persistence;

import com.wallet.ledger.ledger.adapter.out.persistence.repository.AccountRepository;
import com.wallet.ledger.ledger.application.port.out.LoadAccountPort;
import com.wallet.ledger.ledger.domain.DoubleAccountsForLedger;
import com.wallet.ledger.ledger.domain.FinanceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountPersistenceAdapter implements LoadAccountPort {
    private final AccountRepository accountRepository;

    @Override
    public DoubleAccountsForLedger getDoubleAccountsForLedger(FinanceType financeType) {
        return accountRepository.getDoubleAccountsForLedger(financeType);
    }
}
