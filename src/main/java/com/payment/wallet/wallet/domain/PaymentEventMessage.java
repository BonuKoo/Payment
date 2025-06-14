package com.payment.wallet.wallet.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class PaymentEventMessage {
    private PaymentEventMessageType messageType;
    private Map<String, Object> payload = new HashMap<>();
    private Map<String, Object> metadata = new HashMap<>();

    public PaymentEventMessage(PaymentEventMessageType messageType, Map<String, Object> payload) {
        this.messageType = messageType;
        this.payload = payload;
    }

    public String getOrderId(){
        Object value = payload.get("orderId");
        if (value instanceof String){
            return (String) value;
        }
        throw new IllegalStateException("orderId is missing or not a String");
    }

}
