package com.payment.wallet.wallet.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@SuperBuilder
public class Item {

    private int amount;
    private String orderId;
    private Long referenceId;
    private ReferenceType referenceType;

}
