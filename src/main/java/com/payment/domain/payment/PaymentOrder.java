package com.payment.domain.payment;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity @Table(name = "payment_order")
@AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder @Getter
public class PaymentOrder {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "payment_id", nullable = false)
    private PaymentEvent paymentEvent;

    @Column(nullable = false)
    private String sellerId; // 판매자 ID

    @Column(nullable = false)
    private String productId; // 상품 ID

    @Column(name = "orderId")
    private String orderId;

    //orderId
    /*
    @Column(nullable = false)
    private String idempotencyKey;
    */

    @Column(nullable = false)
    private int amount; // 결제 금액

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    /*
        DB ON? NO?
     */
    @Column
    private int failed_count;

    @Column
    private int threshold;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /** todo
     *
     */

    //@Column(nullable = false)
    private boolean isLedgerUpdated; //장부 기입 여부

    //@Column(nullable = false)
    private boolean isWalletUpdated; //정산 처리 여부

    // 메서드
    public boolean isLedgerUpdated() {
        return isLedgerUpdated;
    }

    public boolean isWalletUpdated() {
        return isWalletUpdated;
    }

    public void confirmWalletUpdate() {
        isWalletUpdated = true;
    }

    public void confirmLedgerUpdate() {
        isLedgerUpdated = true;
    }

    public void setPaymentEvent(PaymentEvent paymentEvent) {
        this.paymentEvent = paymentEvent;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}