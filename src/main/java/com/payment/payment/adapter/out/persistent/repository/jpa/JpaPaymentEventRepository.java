package com.payment.payment.adapter.out.persistent.repository.jpa;

import com.payment.domain.payment.PaymentEvent;
import com.payment.domain.payment.PaymentStatus;
import com.payment.payment.adapter.out.persistent.repository.PaymentEventRepository;
import com.payment.payment.adapter.out.persistent.repository.springdata.SpringDataJpaPaymentEventRepository;
import com.payment.payment.domain.PendingPaymentEvent;
import com.payment.payment.domain.PendingPaymentOrder;
import com.payment.payment.domain.PendingPaymentRowDto;
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


    public Optional<PaymentEvent> findByIdempotencyKey(String orderId){
        return springDataJpaPaymentEventRepository.findByIdempotencyKey(orderId);
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

    ;

}
