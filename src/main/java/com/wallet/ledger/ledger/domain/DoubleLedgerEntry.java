package com.wallet.ledger.ledger.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DoubleLedgerEntry {

    private LedgerEntryDTO credit;
    private LedgerEntryDTO debit;
    private LedgerTransactionDTO ledgerTransaction;

    @Builder
    public DoubleLedgerEntry(LedgerEntryDTO credit, LedgerEntryDTO debit, LedgerTransactionDTO ledgerTransaction) {

        if (!credit.getAmount().equals(debit.getAmount())){
            throw new IllegalArgumentException("A double ledger entry requires that the amounts for both the credit and debit are the same.");
        }
        this.credit = credit;
        this.debit = debit;
        this.ledgerTransaction = ledgerTransaction;
    }

}
