package com.payment.wallet.wallet.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "payment_orders")
public class PaymentOrder {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_order_id")
    private Long id;

    @Column(name = "seller_id")
    private Long sellerId;

    private int amount;

    @Column(name = "order_id")
    private String orderId;

}
