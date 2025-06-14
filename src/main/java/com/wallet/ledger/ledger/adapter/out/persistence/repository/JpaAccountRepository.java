package com.wallet.ledger.ledger.adapter.out.persistence.repository;

import com.wallet.ledger.ledger.adapter.out.persistence.entity.Account;
import com.wallet.ledger.ledger.adapter.out.persistence.entity.JpaAccountMapper;
import com.wallet.ledger.ledger.domain.DoubleAccountsForLedger;
import com.wallet.ledger.ledger.domain.FinanceType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaAccountRepository implements AccountRepository{

    private final SpringDataJpaAccountRepository springDataJpaAccountRepository;
    private final JpaAccountMapper jpaAccountMapper;

    private static final String REVENUE_ACCOUNT_NAME = "REVENUE";
    private static final String ITEM_BUYER_ACCOUNT_NAME = "ITEM_BUYER";

    @Override
    public DoubleAccountsForLedger getDoubleAccountsForLedger(FinanceType financeType) {

        switch (financeType){
            case PAYMENT_ORDER :
                Account to = springDataJpaAccountRepository.findByName(REVENUE_ACCOUNT_NAME);
                Account from = springDataJpaAccountRepository.findByName(ITEM_BUYER_ACCOUNT_NAME);
                return new DoubleAccountsForLedger(
                        jpaAccountMapper.mapToAccountDTO(to),
                        jpaAccountMapper.mapToAccountDTO(from)
                );

            default:
                throw new UnsupportedOperationException("Unsupported finance type: " + financeType);
        }
    }
}