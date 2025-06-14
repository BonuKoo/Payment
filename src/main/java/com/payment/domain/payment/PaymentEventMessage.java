package com.payment.domain.payment;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class PaymentEventMessage {

    private PaymentEventMessageType messageType;
    private Map<String, Object> payload;
    private Map<String, Object> metadata;

    @QueryProjection
    public PaymentEventMessage(PaymentEventMessageType messageType, Map<String, Object> payload, Map<String, Object> metadata) {
        this.messageType = messageType;
        this.payload = payload;
        this.metadata = metadata;
    }
}
