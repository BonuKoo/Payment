package com.payment.payment.adapter.out.persistent.repository.jpa;

import com.payment.domain.payment.PaymentOrderHistory;
import com.payment.payment.adapter.out.persistent.repository.PaymentOrderHistoryRepository;
import com.payment.payment.adapter.out.persistent.repository.springdata.SpringDataJpaPaymentOrderHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaPaymentOrderHistoryRepository implements PaymentOrderHistoryRepository {
    private final SpringDataJpaPaymentOrderHistoryRepository springDataJpaPaymentOrderHistoryRepository;

    public void saveAll(List<PaymentOrderHistory> histories){
        springDataJpaPaymentOrderHistoryRepository.saveAll(histories);
    }

}
