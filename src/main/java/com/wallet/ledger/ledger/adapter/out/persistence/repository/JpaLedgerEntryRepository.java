package com.wallet.ledger.ledger.adapter.out.persistence.repository;

import com.wallet.ledger.ledger.adapter.out.persistence.entity.LedgerEntry;
import com.wallet.ledger.ledger.domain.DoubleLedgerEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaLedgerEntryRepository implements LedgerEntryRepository{

    private final SpringDataJpaLedgerEntryRepository springDataJpaLedgerEntryRepository;
    private final JpaLedgerEntryMapper jpaLedgerEntryMapper;

    @Override
    //@Transactional
    public void save(List<DoubleLedgerEntry> doubleLedgerEntries) {
        List<LedgerEntry> entires = doubleLedgerEntries.stream()
                .flatMap(entry -> jpaLedgerEntryMapper.mapToJpaEntity(entry).stream())
                .toList();
        springDataJpaLedgerEntryRepository.saveAll(entires);
    }
}
