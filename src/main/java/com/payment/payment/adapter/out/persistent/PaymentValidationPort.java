package com.payment.payment.adapter.out.persistent;

import java.util.List;

interface PaymentValidationPort{

    boolean isValid(String orderId, long amount);

}
