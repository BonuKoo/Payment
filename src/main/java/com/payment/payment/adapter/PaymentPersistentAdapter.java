package com.payment.payment.adapter;

import com.payment.domain.payment.PaymentEvent;
import com.payment.domain.payment.PaymentEventMessage;
import com.payment.payment.adapter.out.persistent.PaymentStatusUpdateRepository;
import com.payment.payment.adapter.out.persistent.repository.PaymentEventRepository;
import com.payment.payment.adapter.out.persistent.repository.PaymentOrderHistoryRepository;
import com.payment.payment.adapter.out.persistent.repository.PaymentOrderRepository;
import com.payment.payment.adapter.out.persistent.repository.PaymentOutboxRepository;
import com.payment.payment.application.port.out.*;
import com.payment.payment.domain.PaymentEventDto;
import com.payment.payment.domain.PendingPaymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentPersistentAdapter implements SavePaymentPort, PaymentStatusUpdatePort, PaymentValidationPort,
        LoadPendingPaymentPort, LoadPendingPaymentEventMessagePort, LoadPaymentPort, CompletePaymentPort {

    private final PaymentEventRepository paymentEventRepository;
    private final PaymentStatusUpdateRepository paymentStatusUpdateRepository;
    private final PaymentOrderHistoryRepository paymentOrderHistoryRepository;
    private final PaymentOrderRepository paymentOrderRepository;
    private final PaymentOutboxRepository paymentOutboxRepository;

    @Override
    public void save(PaymentEvent paymentEvent) {
        paymentEventRepository.save(paymentEvent);
    }
    @Override
    public Boolean updatePaymentStatusToExecuting(String orderId, String paymentKey) {
        return paymentStatusUpdateRepository.updatePaymentStatusToExecuting(orderId,paymentKey);
    }
    @Override
    public Boolean isValid(String orderId, Long amount) {
        return paymentOrderRepository.isValid(orderId,amount);
    }
    @Override
    public Boolean updatePaymentStatus(PaymentStatusUpdateCommand command) {
        return paymentStatusUpdateRepository.updatePaymentStatus(command);
    }
    @Override
    public List<PendingPaymentEvent> getPendingPayments() {
        return paymentEventRepository.getPendingPayments();
    }
    @Override
    public List<PaymentEventMessage> getPendingPaymentEventMessage() {
        return paymentOutboxRepository.findPendingPaymentOutboxes();
    }

    // todo 추후 orderName -> orderId로 변경
    @Override
    public PaymentEvent getPayment(String orderName) {
        return paymentEventRepository.getPayment(orderName);
    }

    @Override
    public PaymentEventDto getPaymentEventAndOrders(String orderId) {
        return paymentEventRepository.getPaymentEventAndOrders(orderId);
    }

    // TODO
    @Override
    public void complete(PaymentEvent paymentEvent) {

    }

}
