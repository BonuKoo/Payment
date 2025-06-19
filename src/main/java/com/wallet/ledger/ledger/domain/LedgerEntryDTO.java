package com.wallet.ledger.ledger.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LedgerEntryDTO {

    private AccountDTO account;
    private Integer amount;
    private LedgerEntryType type;

    @Builder
    public LedgerEntryDTO(AccountDTO account, int amount, LedgerEntryType type) {
        this.account = account;
        this.amount = amount;
        this.type = type;
    }
}
