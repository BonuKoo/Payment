package com.payment.payment.adapter.in.web.api;

import com.payment.payment.adapter.in.web.request.TossPaymentConfirmRequest;
import com.payment.payment.adapter.in.web.response.ApiResponse;
import com.payment.payment.application.port.in.PaymentConfirmCommand;
import com.payment.payment.application.port.in.PaymentConfirmUseCase;
import com.payment.payment.domain.PaymentConfirmationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/toss")
@RestController
@RequiredArgsConstructor
@Slf4j
public class TossPaymentController {

    private final PaymentConfirmUseCase paymentConfirmUseCase;

    @PostMapping("/confirm")
    public ResponseEntity<ApiResponse<PaymentConfirmationResult>> confirm(@RequestBody TossPaymentConfirmRequest request) {
        PaymentConfirmCommand command = PaymentConfirmCommand.builder()
                .paymentKey(request.getPaymentKey())
                .orderId(request.getOrderId())
                .amount(request.getAmount())
                .build();

        try {
            log.info("Calling confirm use case with command: {}", command);
            PaymentConfirmationResult result = paymentConfirmUseCase.confirm(command);
            log.info("Payment confirmation result: {}", result);
            return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "", result));
        } catch (Exception e) {

            log.error("Payment confirmation error", e);
            // 에러 핸들링 방식에 따라 수정 필요
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.with(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        }
    }
}