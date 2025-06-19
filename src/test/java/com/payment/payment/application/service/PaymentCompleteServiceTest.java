package com.payment.payment.application.service;

import com.payment.payment.application.port.in.CheckoutUseCase;
import com.payment.payment.application.port.in.PaymentCompleteUseCase;
import com.payment.payment.application.port.out.PaymentStatusUpdatePort;
import com.payment.payment.application.port.out.PaymentValidationPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
//@SpringBootTest
class PaymentCompleteServiceTest {
/*
    @Autowired
    private PaymentCompleteUseCase paymentCompleteUseCase;

    @Autowired
    private PaymentDatabaseHelper paymentDatabaseHelper;

    @Test
    void shouldUpdatePaymentGivenWalletEventMessage() {
        String orderId = createPaymentEventWithSuccessStatus();

        WalletEventMessage walletEventMessage = new WalletEventMessage(
                WalletEventMessageType.SUCCESS,
                Map.of("orderId", orderId)
        );

        paymentCompleteUseCase.completePayment(walletEventMessage);

        PaymentEvent paymentEvent = paymentDatabaseHelper.getPayments(orderId);
        assertNotNull(paymentEvent);
        assertTrue(paymentEvent.isWalletUpdateDone());
        assertFalse(paymentEvent.isLedgerUpdateDone());
        assertFalse(paymentEvent.isPaymentDone());
    }

    @Test
    void shouldUpdatePaymentGivenLedgerEventMessage() {
        String orderId = createPaymentEventWithSuccessStatus();

        LedgerEventMessage ledgerEventMessage = new LedgerEventMessage(
                LedgerEventMessageType.SUCCESS,
                Map.of("orderId", orderId)
        );

        paymentCompleteUseCase.completePayment(ledgerEventMessage);

        PaymentEvent paymentEvent = paymentDatabaseHelper.getPayments(orderId);
        assertNotNull(paymentEvent);
        assertTrue(paymentEvent.isLedgerUpdateDone());
        assertFalse(paymentEvent.isWalletUpdateDone());
        assertFalse(paymentEvent.isPaymentDone());
    }

    @Test
    void shouldUpdatePaymentGivenLedgerAndWalletEventMessages() {
        String orderId = createPaymentEventWithSuccessStatus();

        LedgerEventMessage ledgerEventMessage = new LedgerEventMessage(
                LedgerEventMessageType.SUCCESS,
                Map.of("orderId", orderId)
        );
        WalletEventMessage walletEventMessage = new WalletEventMessage(
                WalletEventMessageType.SUCCESS,
                Map.of("orderId", orderId)
        );

        paymentCompleteUseCase.completePayment(ledgerEventMessage);
        paymentCompleteUseCase.completePayment(walletEventMessage);

        PaymentEvent paymentEvent = paymentDatabaseHelper.getPayments(orderId);
        assertNotNull(paymentEvent);
        assertTrue(paymentEvent.isLedgerUpdateDone());
        assertTrue(paymentEvent.isWalletUpdateDone());
        assertTrue(paymentEvent.isPaymentDone());
    }

    private String createPaymentEventWithSuccessStatus() {
        String orderId = UUID.randomUUID().toString();

        CheckoutCommand checkoutCommand = new CheckoutCommand(
                1L,
                1L,
                List.of(1L, 2L, 3L),
                orderId
        );

        CheckoutResult checkoutResult = checkoutUseCase.checkout(checkoutCommand);
        assertNotNull(checkoutResult);

        PaymentConfirmCommand confirmCommand = new PaymentConfirmCommand(
                UUID.randomUUID().toString(),
                orderId,
                checkoutResult.getAmount()
        );

        PaymentExecutionResult executionResult = new PaymentExecutionResult(
                confirmCommand.getPaymentKey(),
                confirmCommand.getOrderId(),
                new PaymentExtraDetails(
                        PaymentType.NORMAL,
                        PaymentMethod.EASY_PAY,
                        confirmCommand.getAmount(),
                        "test_order_name",
                        PSPConfirmationStatus.DONE,
                        LocalDateTime.now(),
                        "{}"
                ),
                true, false, false, false
        );

        when(mockPaymentExecutorPort.execute(confirmCommand))
                .thenReturn(Mono.just(executionResult)); // Optional: if using Reactor

        PaymentConfirmService confirmService = new PaymentConfirmService(
                paymentStatusUpdatePort,
                paymentValidationPort,
                mockPaymentExecutorPort,
                paymentErrorHandler
        );

        confirmService.confirm(confirmCommand); // blocking or synchronous

        return orderId;
    }*/

}