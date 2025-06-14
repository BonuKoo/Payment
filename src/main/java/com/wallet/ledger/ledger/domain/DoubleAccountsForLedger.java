package com.wallet.ledger.ledger.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DoubleAccountsForLedger {
    private AccountDTO to;
    private AccountDTO from;

    public DoubleAccountsForLedger(AccountDTO to) {
        this.to = to;
    }

    @Builder
    public DoubleAccountsForLedger(AccountDTO to, AccountDTO from) {
        this.to = to;
        this.from = from;
    }
}

/**
    자금이 어떤 계정에서, 어떤 계정으로 이동했는 지 나타내기 위해
 To Field와 From Field를 사용
 */