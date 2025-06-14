package com.wallet.ledger.ledger.adapter.out.persistence.repository;

import com.wallet.ledger.ledger.adapter.out.persistence.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaAccountRepository extends JpaRepository<Account,Long> {

    Account findByName(String name);

}
