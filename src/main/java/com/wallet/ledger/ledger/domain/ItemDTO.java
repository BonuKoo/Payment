package com.wallet.ledger.ledger.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemDTO {

    protected Long id;
    protected int amount;
    protected String orderId;
    protected ReferenceType type;

    public ItemDTO(Long id, int amount, String orderId) {
        this.id = id;
        this.amount = amount;
        this.orderId = orderId;
    }

    public ItemDTO(Long id, int amount, String orderId, ReferenceType type) {
        this.id = id;
        this.amount = amount;
        this.orderId = orderId;
        this.type = type;
    }
}
