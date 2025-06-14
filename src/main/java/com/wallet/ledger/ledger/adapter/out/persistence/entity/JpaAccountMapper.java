package com.wallet.ledger.ledger.adapter.out.persistence.entity;

import com.wallet.ledger.ledger.domain.AccountDTO;
import org.springframework.stereotype.Component;

@Component
public class JpaAccountMapper {

    public AccountDTO mapToAccountDTO(Account account){
        return AccountDTO.builder()
                .id(account.getId())
                .name(account.getName())
                .build();
    }

}
