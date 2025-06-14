package com.wallet.ledger.ledger.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class LedgerEventMessage {

    private LedgerEventMessageType messageType;
    private Map<String, Object> payload = new HashMap<>();
    private Map<String, Object> metadata = new HashMap<>();

    @Builder
    public LedgerEventMessage(LedgerEventMessageType messageType, Map<String, Object> payload) {
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
