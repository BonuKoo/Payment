package com.payment.payment.adapter.out.persistent.repository.jpa;

import com.payment.domain.payment.PaymentEvent;
import com.payment.domain.payment.PaymentStatus;
import com.payment.payment.adapter.out.persistent.repository.PaymentEventRepository;
import com.payment.payment.adapter.out.persistent.repository.springdata.SpringDataJpaPaymentEventRepository;
import com.payment.payment.domain.PaymentEventDto;
import com.payment.payment.domain.PendingPaymentEvent;
import com.payment.payment.domain.PendingPaymentOrder;
import com.payment.payment.domain.PendingPaymentRowDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpaPaymentEventRepository implements PaymentEventRepository {

    private final SpringDataJpaPaymentEventRepository springDataJpaPaymentEventRepository;

    @Override
    public void save(PaymentEvent paymentEvent) {
        springDataJpaPaymentEventRepository.save(paymentEvent);
    }


    public Optional<PaymentEvent> findByOrderId(String orderId){
        return springDataJpaPaymentEventRepository.findByOrderId(orderId);
    }

    @Override
    public List<PendingPaymentEvent> getPendingPayments() {
        List<PendingPaymentRowDto> rows = springDataJpaPaymentEventRepository.findPendingPaymentRows(LocalDateTime.now());

        return rows.stream()
                .collect(Collectors.groupingBy(PendingPaymentRowDto::getPaymentEventId))
                .entrySet().stream()
                .map(entry -> {
                    List<PendingPaymentRowDto> group = entry.getValue();
                    PendingPaymentRowDto first = group.get(0);

                    List<PendingPaymentOrder> orders = group.stream()
                            .map(r -> new PendingPaymentOrder(
                                    r.getPaymentOrderId(),
                                    PaymentStatus.get(r.getPaymentOrderStatus()),
                                    r.getAmount().longValue(),
                                    r.getFailedCount(),
                                    r.getThreshold()
                            ))
                            .collect(Collectors.toList());

                    return new PendingPaymentEvent(
                            first.getPaymentEventId(),
                            first.getPaymentKey(),
                            first.getOrderId(),
                            orders
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<PendingPaymentRowDto> findPendingPaymentRows(LocalDateTime now) {
        return springDataJpaPaymentEventRepository.findPendingPaymentRows(now);
    }

    @Override
    public PaymentEvent getPayment(String orderName) {
        return springDataJpaPaymentEventRepository.findByOrderName(orderName).orElseThrow();
    }

    @Override
    public PaymentEventDto getPaymentEventAndOrders(String orderId){
        return springDataJpaPaymentEventRepository.getPaymentEventAndOrders(orderId);
    }

    @Override
    @Transactional
    public void complete(PaymentEventDto paymentEventDto) {
        if (paymentEventDto.isPaymentDone()) {
            // 모든 업데이트 및 완료 처리 포함
            springDataJpaPaymentEventRepository.handlePaymentCompletion(paymentEventDto);
        } else if (paymentEventDto.isWalletUpdateDone()) {
            springDataJpaPaymentEventRepository.handleWalletUpdate(paymentEventDto);
        } else if (paymentEventDto.isLedgerUpdateDone()) {
            springDataJpaPaymentEventRepository.handleLedgerUpdate(paymentEventDto);
        } else {
            throw new IllegalStateException("Incorrect state for PaymentEvent id: " + paymentEventDto.getId());
        }
    }
    ;
}
