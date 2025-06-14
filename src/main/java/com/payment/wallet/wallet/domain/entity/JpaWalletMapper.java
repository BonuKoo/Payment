package com.payment.wallet.wallet.domain.entity;

import com.payment.wallet.wallet.domain.WalletDTO;
import com.payment.wallet.wallet.domain.WalletTransactionDTO;
import org.springframework.stereotype.Component;

@Component
public class JpaWalletMapper {

    public WalletDTO mapToDomainEntity(Wallet wallet){
        return WalletDTO.builder()
                .id(wallet.getId())
                .userId(wallet.getUserId())
                .version(wallet.getVersion())
                .balance(wallet.getBalance())
                .build();
    }

    public Wallet mapToJpaEntity(WalletDTO walletDTO){
        return new Wallet(
                walletDTO.getId(),
                walletDTO.getUserId(),
                walletDTO.getBalance(),
                walletDTO.getVersion()
                );

    }

    public WalletTransaction mapToJpaTransactionEntity(WalletTransactionDTO dto) {

        // Wallet 객체를 DTO에서 직접 추출할 수 없다면, Wallet 객체를 따로 주입하거나 ID만 세팅하는 방식으로 처리
        Wallet wallet = new Wallet(dto.getWalletDTO().getId(), null, null, null);

        return new WalletTransaction(
                dto.getIdempotencyKey(),
                dto.getOrderId(),
                dto.getReferenceId(),
                dto.getReferenceType(),
                dto.getType(),
                dto.getAmount(),
                wallet,
                dto.getId()
        );
    }

    public WalletTransaction mapToJpaTransactionEntity2(WalletTransactionDTO dto, Wallet wallet) {

        return new WalletTransaction(
                dto.getIdempotencyKey(),
                dto.getOrderId(),
                dto.getReferenceId(),
                dto.getReferenceType(),
                dto.getType(),
                dto.getAmount(),
                wallet,
                dto.getId()
        );
    }



}
