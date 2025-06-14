package com.payment.wallet.wallet.adapter.out.persistence.repository;

import com.payment.wallet.wallet.domain.Item;
import com.payment.wallet.wallet.domain.ReferenceType;
import com.payment.wallet.wallet.domain.WalletDTO;
import com.payment.wallet.wallet.domain.entity.JpaWalletMapper;
import com.payment.wallet.wallet.domain.entity.Wallet;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;



@SpringBootTest
class JpaWalletRepositoryTest {
    @Autowired private JpaWalletRepository walletRepository;
    @Autowired private SpringDataJpaWalletRepository springDataJpaWalletRepository;
    @Autowired private SpringDataJpaWalletTransactionRepository springDataJpaWalletTransactionRepository;
    @Autowired private JpaWalletMapper jpaWalletMapper;

    @BeforeEach
    void clean() {
        springDataJpaWalletTransactionRepository.deleteAll();
        springDataJpaWalletRepository.deleteAll();
    }

    @RepeatedTest(value = 15)
    void should_update_the_balance_of_wallet_successfully_when_execute_the_updated_command_at_the_same_time() throws ExecutionException, InterruptedException {

        List<Wallet> jpaWalletEntities = Arrays.asList(
                new Wallet(1L, BigDecimal.ZERO, 0),
                new Wallet(2L, BigDecimal.ZERO, 0)
        );

        List<Wallet> walletList = springDataJpaWalletRepository.saveAll(jpaWalletEntities);

        // 저장된 Entity를 Wallet DTO로 변환
        Wallet wallet1 = walletList.get(0);
        Wallet wallet2 = walletList.get(1);

        WalletDTO walletDTO1 = jpaWalletMapper.mapToDomainEntity(wallet1);
        WalletDTO walletDTO2 = jpaWalletMapper.mapToDomainEntity(wallet2);

        List<Item> itemList1 = Arrays.asList(
                new Item(1000L,
                        UUID.randomUUID().toString(),
                        1L,
                        ReferenceType.PAYMENT_ORDER
                        )
        );
        List<Item> itemList2 = Arrays.asList(
                new Item(2000L,
                        UUID.randomUUID().toString(),
                        2L,
                        ReferenceType.PAYMENT_ORDER
                )
        );
        List<Item> itemList3 = Arrays.asList(
                new Item(3000L,
                        UUID.randomUUID().toString(),
                        3L,
                        ReferenceType.PAYMENT_ORDER
                )
        );

        WalletDTO updatedWallet1 = walletDTO1.calculateBalanceWith(itemList1);  //1000
        WalletDTO updatedWallet2 = walletDTO1.calculateBalanceWith(itemList2);  //2000
        WalletDTO updatedWallet3 = walletDTO1.calculateBalanceWith(itemList3);  //3000

        WalletDTO updatedWallet4 = walletDTO2.calculateBalanceWith(itemList1);  //1000
        WalletDTO updatedWallet5 = walletDTO2.calculateBalanceWith(itemList2);  //2000
        WalletDTO updatedWallet6 = walletDTO2.calculateBalanceWith(itemList3);  //3000

        List<WalletDTO> list1 = List.of(updatedWallet1,updatedWallet4);
        List<WalletDTO> list2 = List.of(updatedWallet2,updatedWallet5);
        List<WalletDTO> list3 = List.of(updatedWallet3,updatedWallet6);

        /**
         * 멀티 쓰레드 환경 테스트
         */
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        Future<?> future1 = executorService.submit(() -> walletRepository.update(list1));
        Future<?> future2 = executorService.submit(() -> walletRepository.update(list2));
        Future<?> future3 = executorService.submit(() -> walletRepository.update(list3));

        future1.get();
        future2.get();
        future3.get();

        Wallet retrievedWallet1 = springDataJpaWalletRepository.findById(walletDTO1.getId()).get();
        Wallet retrievedWallet2 = springDataJpaWalletRepository.findById(walletDTO2.getId()).get();

        Assertions.assertThat(retrievedWallet1.getVersion()).isEqualTo(3);
        Assertions.assertThat(retrievedWallet2.getVersion()).isEqualTo(3);

        Assertions.assertThat(retrievedWallet1.getBalance().toBigInteger()).isEqualTo(6000);
        Assertions.assertThat(retrievedWallet2.getBalance().toBigInteger()).isEqualTo(6000);
    }


}