package com.wallet.ledger.ledger.application.port.out;


import com.wallet.ledger.ledger.domain.DoubleAccountsForLedger;
import com.wallet.ledger.ledger.domain.FinanceType;

/**
 * 결제 거래 유형에 따라 해당되는 결제들을 가져오는 메서드
 * 누가 누구에게 자금을 전송했는 지를 나타낸다.
 */

public interface LoadAccountPort {
    DoubleAccountsForLedger getDoubleAccountsForLedger(FinanceType financeType);
}

