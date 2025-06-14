package com.wallet.ledger.ledger.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountDTO {

    private Long id;
    private String name;

    @Builder
    public AccountDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
