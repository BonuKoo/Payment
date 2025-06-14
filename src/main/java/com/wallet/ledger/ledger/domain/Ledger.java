package com.wallet.ledger.ledger.domain;

import java.util.List;

public class Ledger {

    public static List<DoubleLedgerEntry> createDoubleLedgerEntry(DoubleAccountsForLedger doubleAccountsForLedger, List<ItemDTO> itemDTOS){

        return itemDTOS.stream()
                .map(itemDTO ->
                        new DoubleLedgerEntry(
                        LedgerEntryDTO.builder()
                                .account(doubleAccountsForLedger.getTo())
                                .amount(itemDTO.getAmount())
                                .type(LedgerEntryType.CREDIT)
                                .build()
                        ,
                        LedgerEntryDTO.builder()
                                .account(doubleAccountsForLedger.getFrom())
                                .amount(itemDTO.getAmount())
                                .type(LedgerEntryType.DEBIT)
                                .build()
                        ,
                        LedgerTransactionDTO.builder()
                                .referenceId(itemDTO.getId())
                                .referenceType(itemDTO.getType())
                                .orderId(itemDTO.getOrderId())
                                .build()

                ))
                .toList();

    }

}
