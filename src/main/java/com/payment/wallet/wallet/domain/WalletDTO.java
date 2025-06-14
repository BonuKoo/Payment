package com.payment.wallet.wallet.domain;

import com.payment.wallet.wallet.domain.entity.WalletTransaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletDTO {

    private Long id;
    private Long userId;
    private Integer version;
    private BigDecimal balance;
    private List<WalletTransactionDTO> walletTransactionList;

    //WalletDTO
    public WalletDTO calculateBalanceWith(List<Item> items){

        // 총합 계산
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Item item : items){
            totalAmount = totalAmount.add(BigDecimal.valueOf(item.getAmount()));
        }

        // WalletTransactionDTO List 생성
        List<WalletTransactionDTO> walletTransactions = items.stream()
                .map(item -> WalletTransactionDTO.builder()
                        .walletDTO(this)
                        .amount(BigDecimal.valueOf(item.getAmount()))
                        .type(TransactionType.CREDIT)
                        .referenceId(item.getReferenceId())
                        .referenceType(item.getReferenceType())
                        .orderId(item.getOrderId())
                        .build()
                ).toList();

        // 3. 새로운 WalletDTO 반환
        return WalletDTO.builder()
                .id(this.id)
                .userId(this.userId)
                .version(this.version)
                .balance(this.balance.add(totalAmount)) // balance + 총합
                .walletTransactionList(walletTransactions)
                .build();
    }

}
