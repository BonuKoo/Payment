package com.payment.wallet.wallet.adapter.out.persistence.repository;

import com.payment.wallet.wallet.adapter.out.persistence.exception.RetryExhaustedWithOptimisticLockingFailureException;
import com.payment.wallet.wallet.domain.WalletDTO;
import com.payment.wallet.wallet.domain.WalletTransactionDTO;
import com.payment.wallet.wallet.domain.entity.JpaWalletMapper;
import com.payment.wallet.wallet.domain.entity.Wallet;
import com.payment.wallet.wallet.domain.entity.WalletTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpaWalletRepository implements WalletRepository{

   private final SpringDataJpaWalletRepository springDataJpaWalletRepository;
   private final WalletTransactionRepository walletTransactionRepository;
   private final JpaWalletMapper jpaWalletMapper;
   private final TransactionTemplate transactionTemplate;

   @Override
    public Set<WalletDTO> getWallets(Set<Long> sellerIds) {
       return springDataJpaWalletRepository.findByUserIdIn(sellerIds).stream()
                .map(jpaWalletMapper::mapToDomainEntity)
                .collect(Collectors.toSet());
    }

//  @Transactional
    @Override
    public void save(List<WalletDTO> wallets) {
        try {
        performSaveOperation(wallets);
        } catch (ObjectOptimisticLockingFailureException e){
            /**
             * ObjectOptimisticLockingFailureException 오류가 발생했을 시
             * */
            retrySaveOperationV1(wallets);
        }
   }
    /**
     transactionTemplate.execute()는 반환값 없이 실행하는 람다를 기대
     */
    private void performSaveOperation(List<WalletDTO> wallets) {

        // ID 정렬 - 락 순서 고정
        List<Long> walletIds = wallets.stream()
                .map(WalletDTO::getId)
                .distinct()
                .sorted()
                .toList();

        List<Wallet> walletsToUpdate = springDataJpaWalletRepository.findByIdIn(new HashSet<>(walletIds));
        Map<Long, Wallet> walletMap = walletsToUpdate.stream()
                .collect(Collectors.toMap(Wallet::getId, Function.identity()));

        // DTO 정보 반영
        List<WalletTransaction> allTransactions = new ArrayList<>();

        for (WalletDTO dto : wallets){
            Wallet wallet = walletMap.get(dto.getId());
            if (wallet == null) {
                throw new IllegalArgumentException("Wallet not found: " + dto.getId());
            }
            //balance 변경
            wallet.setBalance(dto.getBalance());

            // 트랜잭션 매핑
            List<WalletTransaction> transactions = dto.getWalletTransactionList().stream()
                    .map(tx -> jpaWalletMapper.mapToJpaTransactionEntity2(tx, wallet))
                    .toList();

            allTransactions.addAll(transactions);
        }

        transactionTemplate.execute(status -> {
                    springDataJpaWalletRepository.saveAll(walletMap.values());
                    walletTransactionRepository.save(allTransactions);
            return null;
        }
        );
    }

    private void retrySaveOperationV1(List<WalletDTO> wallets) {

        int maxRetries = 5;
        int baseDelayMillis = 50;

        for (int retry = 1; retry <= maxRetries; retry++) {
            try{
                performSaveOperationWithRecentV2(wallets);
                break;  // 성공 시 탈출
            } catch (ObjectOptimisticLockingFailureException e){
                if (retry == maxRetries) {
                    throw new RetryExhaustedWithOptimisticLockingFailureException(
                            "Optimistic locking failed after " + maxRetries + " retries: " + e.getMessage());
                }
                waitForNextRetryV1(baseDelayMillis, retry-1);
            }
        }
    }

    private void waitForNextRetryV1(int baseDelayMillis, int retryCount) {
        long exponentialBackoff = baseDelayMillis * (1L << retryCount);
        long jitter = (long)(Math.random() * (baseDelayMillis * 2));  // jitter를 base보다 크게 주기
        int maxDelayMillis = 1000;
        long sleepTime = Math.min(exponentialBackoff + jitter, maxDelayMillis);

        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted during retry wait",e);
        }
    }

    private void retrySaveOperationV2(List<WalletDTO> wallets) {
        int maxRetries = 5;
        int baseDelayMillis = 50;

        for (int retry = 1; retry <= maxRetries; retry++) {
            try {
                performSaveOperationWithRecentV2(wallets);
                return;
            } catch (ObjectOptimisticLockingFailureException e) {
                if (retry == maxRetries) {
                    throw new RetryExhaustedWithOptimisticLockingFailureException(
                            "Optimistic locking failed after " + maxRetries + " retries. Last error: " + e.getMessage());
                }
                waitForNextRetryV2(baseDelayMillis, retry - 1);
            }
        }
    }

    private void waitForNextRetryV2(int baseDelayMillis, int retryCount) {
        long exponentialBackoff = baseDelayMillis * (1L << retryCount);
        long jitter = (long)(Math.random() * (baseDelayMillis * 2));
        int maxDelayMillis = 1000;
        long sleepTime = Math.min(exponentialBackoff + jitter, maxDelayMillis);

        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted during retry wait", e);
        }
    }


    /**
     * 최신 상태의 지갑을 Update
     * */
    private void performSaveOperationWithRecentV1(List<WalletDTO> wallets) {

        // WalletDTO List 를 Id를 Index로 Map 화
        // Sorted -> 락 획득 순서 고정
        List<Long> walletIds = wallets.stream()
                .map(WalletDTO::getId)
                .distinct()
                .sorted()
                .toList();

        // 조회
        List<Wallet> recentWallets = springDataJpaWalletRepository.findByIdIn(new HashSet<>(walletIds));

        Map<Long, Wallet> recentWalletsById = recentWallets.stream()
                .collect(Collectors.toMap(Wallet::getId, Function.identity()));

        // Entity 변경 준비
        for (WalletDTO walletDTO : wallets) {

            Wallet wallet = recentWalletsById.get(walletDTO.getId());

            if (wallet == null) {
                throw new IllegalArgumentException("Wallet not found: " + walletDTO.getId());
            }

            // 트랜잭션 금액 총합 계산
            BigDecimal amountToAdd = walletDTO.getWalletTransactionList().stream()
                    .map(WalletTransactionDTO::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // 기존 balance에 더함
            wallet.addBalance(amountToAdd); // addBalance 메서드를 엔티티에 추가

        }

        transactionTemplate.execute(
                status -> {
                    // 변경된 Wallet 저장
                    springDataJpaWalletRepository.saveAll(recentWalletsById.values());

                    // WalletTransaction
                    // ID가 일치하는 Wallet을 찾음
                    for (WalletDTO dto : wallets){
                        Wallet wallet = recentWalletsById.get(dto.getId());
                        List<WalletTransaction> transactions = dto.getWalletTransactionList().stream()
                                .map(tx -> jpaWalletMapper.mapToJpaTransactionEntity2(tx, wallet))
                                .toList();
                        walletTransactionRepository.save(transactions);
                    }

                    return null;
                }
        );
    }

    /**
     * V1에 비해 성공률이 3/15 -> 6/15로 상승.
     * 트랜잭션 경계 안에서 최신 데이터를 조회
     * 조회한 엔티티를 수정 후 저장
     *
     * 이전에는 엔티티 조회 ( findByIdIn ) 가 트랜잭션 밖에서 일어나서, 그 사이에 다른 트랜잭션에 데이터를 수정하면
     * 낙관적 락 충돌이 발생
     * V1-> V2 트랜잭션 내부에서 최신 상태로 조회 -> 수정 -> 저장을 수행하니 데이터 일관성 보장 강화
     * @param wallets
     */
    private void performSaveOperationWithRecentV2(List<WalletDTO> wallets) {

        // WalletDTO List를 Id를 Index로 Map화 (정렬 및 중복 제거)
        List<Long> walletIds = wallets.stream()
                .map(WalletDTO::getId)
                .distinct()
                .sorted()
                .toList();

        transactionTemplate.execute(status -> {
            // 트랜잭션 안에서 최신 Wallet 조회
            List<Wallet> recentWallets = springDataJpaWalletRepository.findByIdIn(new HashSet<>(walletIds));

            Map<Long, Wallet> recentWalletsById = recentWallets.stream()
                    .collect(Collectors.toMap(Wallet::getId, Function.identity()));

            // Entity 변경
            for (WalletDTO walletDTO : wallets) {
                Wallet wallet = recentWalletsById.get(walletDTO.getId());

                if (wallet == null) {
                    throw new IllegalArgumentException("Wallet not found: " + walletDTO.getId());
                }

                BigDecimal amountToAdd = walletDTO.getWalletTransactionList().stream()
                        .map(WalletTransactionDTO::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                wallet.addBalance(amountToAdd);
            }

            // 변경된 Wallet 저장
            springDataJpaWalletRepository.saveAll(recentWalletsById.values());

            // WalletTransaction 저장
            for (WalletDTO dto : wallets) {
                Wallet wallet = recentWalletsById.get(dto.getId());
                List<WalletTransaction> transactions = dto.getWalletTransactionList().stream()
                        .map(tx -> jpaWalletMapper.mapToJpaTransactionEntity2(tx, wallet))
                        .toList();
                walletTransactionRepository.save(transactions);
            }

            return null;
        });
    }


    /**
     * 낙관 락 문제 해소용 임시 메서드
     * */
    @Override
/*    public void update(List<WalletDTO> wallets) {

        List<WalletTransaction> allTransactions = new ArrayList<>();

        for (WalletDTO walletDTO : wallets){
            Wallet wallet = springDataJpaWalletRepository.findById(walletDTO.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Wallet not found: " + walletDTO.getId()));

            wallet.setBalance(walletDTO.getBalance());

            // 트랜잭션 엔티티 매핑
            List<WalletTransaction> transactions = walletDTO.getWalletTransactionList().stream()
                    .map(jpaWalletMapper::mapToJpaTransactionEntity)
                    .peek(tx -> tx.setWallet(wallet)) // 연관관계 설정
                    .toList();

            allTransactions.addAll(transactions);

        }
            walletTransactionRepository.save(allTransactions);

    }*/
    public void update(List<WalletDTO> wallets) {
        try {
            updateWithTransaction(wallets, false);
        } catch (ObjectOptimisticLockingFailureException e) {
            retryUpdateOperationForUpdate(wallets);
        }
    }

    private void retryUpdateOperationForUpdate(List<WalletDTO> wallets) {
        int maxRetries = 5;
        int baseDelayMillis = 50;

        for (int retry = 1; retry <= maxRetries; retry++) {
            try {
                updateWithTransaction(wallets, true);
                return;
            } catch (ObjectOptimisticLockingFailureException e) {
                if (retry == maxRetries) {
                    throw new RetryExhaustedWithOptimisticLockingFailureException(
                            "Failed after retries: " + e.getMessage());
                }
                waitForNextRetryV2(baseDelayMillis, retry);
            }
        }
    }

    @Transactional
    public void updateWithTransaction(List<WalletDTO> wallets, boolean recalculateFromTx) {
        List<Long> walletIds = wallets.stream()
                .map(WalletDTO::getId)
                .distinct()
                .sorted()
                .toList();

        Map<Long, Wallet> walletMap = springDataJpaWalletRepository.findByIdIn(new HashSet<>(walletIds))
                .stream()
                .collect(Collectors.toMap(Wallet::getId, Function.identity()));

        for (WalletDTO dto : wallets) {
            Wallet wallet = walletMap.get(dto.getId());
            if (wallet == null) throw new IllegalArgumentException("Wallet not found: " + dto.getId());

            if (recalculateFromTx) {
                BigDecimal total = dto.getWalletTransactionList().stream()
                        .map(WalletTransactionDTO::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                if (total.compareTo(BigDecimal.ZERO) != 0) {
                    wallet.addBalance(total);
                }
            } else {
                if (wallet.getBalance().compareTo(dto.getBalance()) != 0) {
                    wallet.setBalance(dto.getBalance());
                }
            }
        }

        List<WalletTransaction> transactions = wallets.stream()
                .flatMap(dto -> dto.getWalletTransactionList().stream()
                        .map(tx -> jpaWalletMapper.mapToJpaTransactionEntity2(tx, walletMap.get(dto.getId()))))
                .toList();

        springDataJpaWalletRepository.saveAll(walletMap.values());
        walletTransactionRepository.save(transactions);
    }

}
