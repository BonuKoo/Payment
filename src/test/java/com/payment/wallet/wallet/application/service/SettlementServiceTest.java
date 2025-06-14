package com.payment.wallet.wallet.application.service;

import com.payment.wallet.wallet.adapter.out.persistence.repository.JpaWalletRepository;
import com.payment.wallet.wallet.adapter.out.persistence.repository.SpringDataJpaPaymentOrderRepository;
import com.payment.wallet.wallet.adapter.out.persistence.repository.SpringDataJpaWalletRepository;
import com.payment.wallet.wallet.adapter.out.persistence.repository.SpringDataJpaWalletTransactionRepository;
import com.payment.wallet.wallet.application.port.out.DuplicateMessageFilterPort;
import com.payment.wallet.wallet.application.port.out.LoadPaymentOrderPort;
import com.payment.wallet.wallet.application.port.out.LoadWalletPort;
import com.payment.wallet.wallet.application.port.out.SaveWalletPort;
import com.payment.wallet.wallet.domain.*;
import com.payment.wallet.wallet.domain.entity.PaymentOrder;
import com.payment.wallet.wallet.domain.entity.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.mock;

import static org.mockito.Mockito.*;

@SpringBootTest
class SettlementServiceTest {

    @Autowired private DuplicateMessageFilterPort duplicateMessageFilterPort;

    @Autowired private LoadWalletPort loadWalletPort;
    @Autowired private SaveWalletPort saveWalletPort;
    @Autowired private LoadPaymentOrderPort loadPaymentOrderPort;

    @Autowired private SpringDataJpaWalletRepository springDataJpaWalletRepository;
    @Autowired private SpringDataJpaWalletTransactionRepository springDataJpaWalletTransactionRepository;
    @Autowired private SpringDataJpaPaymentOrderRepository springDataJpaPaymentOrderRepository;

    @Autowired private JpaWalletRepository walletRepository;

    @Autowired private SettlementService settlementService;

    @BeforeEach
    void clean() {
        springDataJpaWalletTransactionRepository.deleteAll();
        springDataJpaWalletRepository.deleteAll();
    }

    @Test
    void shouldProcessSettlementSuccessfully() {

        // Given
        List<Wallet> jpaWalletEntities = Arrays.asList(
                new Wallet(1L, BigDecimal.ZERO, 0),
                new Wallet(2L, BigDecimal.ZERO, 0)
        );

        springDataJpaWalletRepository.saveAll(jpaWalletEntities);

        String orderId = UUID.randomUUID().toString();

        PaymentEventMessage paymentEventMessage = new PaymentEventMessage(
                PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS,
                Map.of("orderId", orderId)
        );

        //LoadPaymentOrderPort mockLoadPaymentOrderPort = mock(LoadPaymentOrderPort.class);
        /*
        List<PaymentOrderDTO> paymentOrderList = Arrays.asList(
                new PaymentOrderDTO(1L, 1L, 3000L, orderId),
                new PaymentOrderDTO(2L, 2L, 3000L, orderId)
        );*/

        List<PaymentOrder> paymentOrders = Arrays.asList(
                new PaymentOrder(1L, 1L, new BigDecimal("3000"), orderId),
                new PaymentOrder(2L, 2L, new BigDecimal("4000"), orderId)
        );

        springDataJpaPaymentOrderRepository.saveAll(paymentOrders);

        // When
        WalletEventMessage walletEventMessage = settlementService.processSettlement(paymentEventMessage);

        // orderId 눈으로 확인
        System.out.println(walletEventMessage.getPayload().get(("orderId")));
        //walletEventMessage.getPayload().get()
        System.out.println(orderId);
        // <-- 위 까지 확인

        // Then
        assertThat(walletEventMessage.getPayload().get("orderId")).isEqualTo(orderId);
        assertThat(walletEventMessage.getType()).isEqualTo(WalletEventMessageType.SUCCESS);

        // 검증용 지갑 상태 업데이트

        Set<WalletDTO> updatedWallets = walletRepository.getWallets(Set.of(1L, 2L));



        List<WalletDTO> sortedWallets = updatedWallets.stream()
                .sorted(Comparator.comparing(WalletDTO::getUserId))
                .toList();

        springDataJpaWalletRepository.findAll().forEach(w ->
                System.out.println("wallet userId: " + w.getUserId() + ", balance: " + w.getBalance())
        );

        assertThat(sortedWallets.get(0).getBalance().intValue()).isEqualTo(3000);
        assertThat(sortedWallets.get(1).getBalance().intValue()).isEqualTo(4000);
        assertThat(sortedWallets.get(1).getBalance().intValue()).isEqualTo(3000);
    }

}