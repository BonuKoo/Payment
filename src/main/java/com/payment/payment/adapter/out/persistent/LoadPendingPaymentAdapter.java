package com.payment.payment.adapter.out.persistent;

import com.payment.domain.payment.PaymentStatus;
import com.payment.payment.adapter.out.persistent.repository.springdata.SpringDataJpaPaymentEventRepository;
import com.payment.payment.application.port.out.LoadPendingPaymentPort;
import com.payment.payment.domain.PendingPaymentEvent;
import com.payment.payment.domain.PendingPaymentOrder;
import com.payment.payment.domain.PendingPaymentRowDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LoadPendingPaymentAdapter implements LoadPendingPaymentPort {

    private final SpringDataJpaPaymentEventRepository springDataJpaPaymentEventRepository;

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
}