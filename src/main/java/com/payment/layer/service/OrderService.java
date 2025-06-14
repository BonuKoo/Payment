package com.payment.layer.service;

import com.payment.domain.order.Order;
import com.payment.domain.order.OrderItem;
import com.payment.domain.item.Cart;
import com.payment.domain.item.Item;
import com.payment.layer.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public Order createOrderFromCart(String accountId, Cart cart) {

        // 장바구니에서 주문 항목 생성
        List<OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> {
                    Item item = cartItem.getItem();
                    int count = cartItem.getCount();

                    // 재고 감소
                    item.removeStock(count);

                    // OrderItem 생성
                    return OrderItem.createOrderItem(item, count);
                })
                .toList();

        // 주문 총 금액 계산
        int totalAmount = orderItems.stream()
                .mapToInt(orderItem -> orderItem.getItem().getPrice() * orderItem.getCount())
                .sum();

        // Order 생성
        Order order = Order.builder()
                .accountId(accountId)
                .orderName("주문") // TODO: 주문 이름 설정 로직 필요
                .orderItems(orderItems)
                .totalAmount(totalAmount)
                .orderDate(LocalDateTime.now())
                .build();

        // 주문 저장
        orderRepository.save(order);

        // 장바구니 비우기
        cart.getCartItems().clear();

        return order;
    }
}