package com.wallet.ledger.ledger.application.service;

import com.wallet.ledger.ledger.application.port.in.DoubleLedgerEntryRecordUseCase;
import com.wallet.ledger.ledger.application.port.out.DuplicateMessageFilterPort;
import com.wallet.ledger.ledger.application.port.out.LoadAccountPort;
import com.wallet.ledger.ledger.application.port.out.LoadPaymentOrderPort;
import com.wallet.ledger.ledger.application.port.out.SaveDoubleLedgerEntryPort;
import com.wallet.ledger.ledger.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DoubleLedgerEntryRecordService implements DoubleLedgerEntryRecordUseCase {

    private final DuplicateMessageFilterPort duplicateMessageFilterPort;
    private final LoadAccountPort loadAccountPort;
    private final LoadPaymentOrderPort loadPaymentOrderPort;
    private final SaveDoubleLedgerEntryPort saveDoubleLedgerEntryPort;

    @Override
    public LedgerEventMessage recordDoubleLedgerEntry(PaymentEventMessage message) {
        if (duplicateMessageFilterPort.isAlreadyProcess(message)) {
            return createLedgerEventMessage(message);
        }

        DoubleAccountsForLedger doubleAccountsForLedger = loadAccountPort.getDoubleAccountsForLedger(FinanceType.PAYMENT_ORDER);
        List<PaymentOrderDTO> paymentOrders = loadPaymentOrderPort.getPaymentOrders(message.getOrderId());
        List<ItemDTO> upcastedToItemDtoFromPaymentOrders = new ArrayList<>(paymentOrders);
        List<DoubleLedgerEntry> doubleLedgerEntries = Ledger.createDoubleLedgerEntry(doubleAccountsForLedger, upcastedToItemDtoFromPaymentOrders);

        saveDoubleLedgerEntryPort.save(doubleLedgerEntries);

        return createLedgerEventMessage(message);

    }

    private LedgerEventMessage createLedgerEventMessage(PaymentEventMessage message) {

        return LedgerEventMessage.builder()
                .messageType(LedgerEventMessageType.SUCCESS)
                .payload(Map.of("orderId", message.getOrderId()))
                .build();
    }

}
