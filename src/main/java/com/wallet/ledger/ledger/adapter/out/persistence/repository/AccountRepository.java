package com.wallet.ledger.ledger.adapter.out.persistence.repository;

import com.wallet.ledger.ledger.domain.DoubleAccountsForLedger;
import com.wallet.ledger.ledger.domain.FinanceType;

public interface AccountRepository {

    DoubleAccountsForLedger getDoubleAccountsForLedger(FinanceType financeType);

    }
