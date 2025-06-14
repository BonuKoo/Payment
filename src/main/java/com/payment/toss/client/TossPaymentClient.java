package com.payment.toss.client;

import com.payment.payment.adapter.in.web.request.TossPaymentConfirmRequest;
import com.payment.payment.adapter.out.web.toss.response.TossPaymentConfirmationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "tossPaymentClient", url = "https://api.tosspayments.com")
public interface TossPaymentClient {

    @PostMapping("/v1/payments/confirm")
    public TossPaymentConfirmationResponse confirmPayment(
            @RequestHeader("Idempotency-Key") String orderId,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody TossPaymentConfirmRequest paymentConfirmRequest
    );

}
