package com.payment.domain.payment.dto;

import lombok.Data;

@Data
public class RefundReceiveAccount {
    private String bankCode;
    private String accountNumber;
    private String holderName;
}
