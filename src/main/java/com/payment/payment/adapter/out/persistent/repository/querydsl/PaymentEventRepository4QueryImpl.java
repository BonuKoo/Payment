package com.payment.payment.adapter.out.persistent.repository.querydsl;

import com.payment.domain.payment.PaymentStatus;
import com.payment.domain.payment.QPaymentEvent;
import com.payment.domain.payment.QPaymentOrder;
import com.payment.payment.domain.PendingPaymentRowDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PaymentEventRepository4QueryImpl implements PaymentEventRepository4Query{

    private final JPAQueryFactory queryFactory;
    private QPaymentEvent paymentEvent = QPaymentEvent.paymentEvent;
    private QPaymentOrder paymentOrder = QPaymentOrder.paymentOrder;

    public PaymentEventRepository4QueryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<PendingPaymentRowDto> findPendingPaymentRows(LocalDateTime now) {

        LocalDateTime thresholdTime = now.minusMinutes(3);

        return queryFactory
                .select(Projections.constructor(PendingPaymentRowDto.class,
                        paymentEvent.id,
                        paymentEvent.paymentKey,
                        paymentEvent.idempotencyKey,
                        paymentOrder.id,
                        paymentOrder.paymentStatus,
                        paymentOrder.amount,
                        paymentOrder.failed_count,
                        paymentOrder.threshold
                        ))
                .from(paymentEvent)
                .join(paymentOrder).on(paymentOrder.paymentEvent.eq(paymentEvent))
                .where(
                        paymentOrder.failed_count.lt(paymentOrder.threshold),
                        paymentOrder.paymentStatus.eq(PaymentStatus.UNKNOWN)
                                .or(paymentOrder.paymentStatus.eq(PaymentStatus.EXECUTING)
                                        .and(paymentOrder.updatedAt.loe(thresholdTime)))
                )
                .limit(10)
                .fetch();

    }
}
