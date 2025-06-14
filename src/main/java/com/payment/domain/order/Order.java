package com.payment.domain.order;

import com.payment.domain.payment.PaymentEvent;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "orders")
@AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "account_id")
    private String accountId;

    //주문 이름
    @Column(name = "order_name")
    private String orderName;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    //주문 총 가격
    private int totalAmount;

    //주문시간
    private LocalDateTime orderDate;

    /*
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PaymentEvent paymentEvent; // 결제 정보
    */

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문 상태 (NEW, PAID, CANCELED)

    // 주문 상태 업데이트
    public void updateOrderStatus(OrderStatus newStatus) {
        this.orderStatus = newStatus;
    }


    // 주문에 결제 추가
    /*
    public void setPaymentEvent(PaymentEvent paymentEvent) {
        this.paymentEvent = paymentEvent;
        paymentEvent.setOrder(this);
    }
    */


}
