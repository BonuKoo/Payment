package com.payment.layer.service;

import com.payment.domain.item.Cart;
import com.payment.domain.item.CartItem;
import com.payment.domain.item.Item;
import com.payment.domain.order.Order;
import com.payment.layer.repository.CartRepository;
import com.payment.layer.repository.ItemRepository;
import com.payment.layer.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ItemRepository itemRepository;


    //@Test
    void 장바구니_기반_주문_생성_테스트() {
        // Given: 장바구니, 상품 생성
        String accountId = "test_account";

        Cart cart = new Cart();
        cartRepository.save(cart);

        Item item = new Item("123456", "테스트 상품", 10000, 10);
        itemRepository.save(item);

        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .item(item)
                .count(2)
                .build();
        cart.getCartItems().add(cartItem);
        cartRepository.save(cart);

        // When: 장바구니를 기반으로 주문 생성
        Order order = orderService.createOrderFromCart(accountId, cart);

        // Then: 주문이 정상적으로 생성되었는지 검증
        assertNotNull(order);
        assertThat(order.getAccountId()).isEqualTo(accountId);
        assertThat(order.getTotalAmount()).isEqualTo(20000); // 10000 * 2
        assertThat(order.getOrderItems()).hasSize(1);
        assertThat(orderRepository.findAll()).hasSize(1);

        // 장바구니가 비워졌는지 확인
        assertThat(cart.getCartItems()).isEmpty();
    }
}