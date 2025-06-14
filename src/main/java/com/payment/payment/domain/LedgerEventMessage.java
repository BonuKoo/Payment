package com.payment.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LedgerEventMessage {

    private LedgerEventMessageType type;
    private Map<String, Object> payload = new HashMap<>();
    private Map<String, Object> metadata = new HashMap<>();

    public String getOrderId(){
        Object value = payload.get("orderId");
        if (value instanceof String){
            return (String) value;
        }
        throw new IllegalStateException("orderId is missing or not a String");
    }

}
