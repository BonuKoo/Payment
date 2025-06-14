package com.payment.payment.adapter.out.persistent.repository;


import com.payment.domain.payment.PaymentOrderHistory;

import java.util.List;

public interface PaymentOrderHistoryRepository {

    void saveAll(List<PaymentOrderHistory> histories);

}
