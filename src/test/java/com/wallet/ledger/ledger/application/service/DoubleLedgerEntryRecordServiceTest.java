package com.wallet.ledger.ledger.application.service;

import com.wallet.ledger.ledger.adapter.out.persistence.entity.Account;
import com.wallet.ledger.ledger.adapter.out.persistence.entity.LedgerEntry;
import com.wallet.ledger.ledger.adapter.out.persistence.repository.SpringDataJpaAccountRepository;
import com.wallet.ledger.ledger.adapter.out.persistence.repository.SpringDataJpaLedgerEntryRepository;
import com.wallet.ledger.ledger.adapter.out.persistence.repository.SpringDataJpaLedgerTransactionRepository;
import com.wallet.ledger.ledger.application.port.out.DuplicateMessageFilterPort;
import com.wallet.ledger.ledger.application.port.out.LoadAccountPort;
import com.wallet.ledger.ledger.application.port.out.LoadPaymentOrderPort;
import com.wallet.ledger.ledger.application.port.out.SaveDoubleLedgerEntryPort;
import com.wallet.ledger.ledger.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class DoubleLedgerEntryRecordServiceTest {

    /*
    private final DuplicateMessageFilterPort duplicateMessageFilterPort = mock(DuplicateMessageFilterPort.class);
    private final LoadAccountPort loadAccountPort = mock(LoadAccountPort.class);
    private final SaveDoubleLedgerEntryPort saveDoubleLedgerEntryPort = mock(SaveDoubleLedgerEntryPort.class);
    private final SpringDataJpaLedgerEntryRepository springDataJpaLedgerEntryRepository = mock(SpringDataJpaLedgerEntryRepository.class);
    */

    @Autowired private SpringDataJpaLedgerEntryRepository springDataJpaLedgerEntryRepository;
    @Autowired private SpringDataJpaLedgerTransactionRepository springDataJpaLedgerTransactionRepository;
    @Autowired private DuplicateMessageFilterPort duplicateMessageFilterPort;
    @Autowired private LoadAccountPort loadAccountPort;
    @Autowired private SaveDoubleLedgerEntryPort saveDoubleLedgerEntryPort;

    @Autowired private SpringDataJpaAccountRepository springDataJpaAccountRepository;


    private final LoadPaymentOrderPort loadPaymentOrderPort = mock(LoadPaymentOrderPort.class);


    //@BeforeEach
    void clean(){
        springDataJpaLedgerEntryRepository.deleteAll();
        springDataJpaLedgerTransactionRepository.deleteAll();

//        springDataJpaAccountRepository.save(new Account("REVENUE"));
//        springDataJpaAccountRepository.save(new Account("ITEM_BUYER"));
    }

    @Test
    void should_record_double_ledger_entries_successfully() {

        // given
        String orderId = UUID.randomUUID().toString();

        PaymentEventMessage paymentEventMessage = new PaymentEventMessage(
                PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS,
                Map.of("orderId", orderId)
        );

        when(loadPaymentOrderPort.getPaymentOrders(orderId)).thenReturn(List.of(
                new PaymentOrderDTO(1L, 200L, orderId),
                new PaymentOrderDTO(2L, 300L, orderId)
        ));

        DoubleLedgerEntryRecordService service = new DoubleLedgerEntryRecordService(
                duplicateMessageFilterPort,
                loadAccountPort,
                loadPaymentOrderPort,
                saveDoubleLedgerEntryPort
        );

        // when
        LedgerEventMessage ledgerEventMessage = service.recordDoubleLedgerEntry(paymentEventMessage);

        List<LedgerEntry> ledgerEntries = springDataJpaLedgerEntryRepository.findAll();

        BigDecimal sumOf = ledgerEntries.stream()
                .map(entry -> {
                    if (entry.getType() == LedgerEntryType.CREDIT) {
                        return entry.getAmount();
                    } else {
                        return entry.getAmount().negate();
                    }
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // then
        assertThat(ledgerEventMessage.getMessageType()).isEqualTo(LedgerEventMessageType.SUCCESS);
        assertThat(ledgerEventMessage.getOrderId()).isEqualTo(paymentEventMessage.getOrderId());
        assertThat(sumOf.compareTo(BigDecimal.ZERO)).isEqualTo(0);
        assertThat(ledgerEntries.size()).isEqualTo(4);


    }

}