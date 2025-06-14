package com.payment.wallet.wallet.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
//@AllArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // 한 유저 당 하나의 지갑 이므로 unique=true
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    // 지갑 잔액
    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    // 낙관적 락 Optimistic Lock, 동시성 처리에 사용되어 잔액 업데이트 시 충돌 방지.
    @Version
    private Integer version;

    public Wallet(Long userId, BigDecimal balance, Integer version) {
        this.userId = userId;
        this.balance = balance;
        this.version = version;
    }

    public Wallet(Long id, Long userId, BigDecimal balance, Integer version) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
        this.version = version;
    }

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void addBalance(BigDecimal amount) {



        this.balance = this.balance.add(amount);
    }

    /*
    @PrePersist
    public void prePersist() {
        this.createdAt = this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }*/

}
