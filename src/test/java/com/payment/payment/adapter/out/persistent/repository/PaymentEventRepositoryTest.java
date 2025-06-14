package com.payment.payment.adapter.out.persistent.repository;

import com.payment.domain.item.Cart;
import com.payment.domain.item.CartItem;
import com.payment.domain.item.Item;
import com.payment.domain.payment.PaymentEvent;
import com.payment.domain.payment.PaymentMethod;
import com.payment.domain.payment.PaymentOrder;
import com.payment.domain.payment.PaymentStatus;
import com.payment.layer.repository.CartRepository;
import com.payment.layer.repository.ItemRepository;
import com.payment.layer.service.CartItemService;
import com.payment.payment.adapter.out.persistent.repository.springdata.SpringDataJpaPaymentEventRepository;
import com.payment.payment.adapter.out.persistent.repository.springdata.SpringDataJpaPaymentOrderRepository;
import com.payment.payment.util.IdempotencyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//@SpringBootTest
@Transactional
class PaymentEventRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private SpringDataJpaPaymentEventRepository springDataJpaPaymentEventRepository;

    @Autowired
    private SpringDataJpaPaymentOrderRepository springDataJpaPaymentOrderRepository;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ItemRepository itemRepository;
    
    // Payment Event & Order 저장 확인
    
    //@Test
    void checkoutTest(){
        // 구매자
        Long buyerId = 10L;

        // 카트
        Cart cart = Cart.builder()
                .buyerId(buyerId)
                .build();

        // 아이템

//        Item item = new Item("1", "테스트 상품", 10000, 10);
        Item item = Item.builder()
                .isbn("1")
                .title("테스트 상품")
                .price(10000)
                .stockQuantity(10)
                .sellerId("3")
                .build();

        itemRepository.save(item);

        Optional<Item> byIsbn = itemRepository.findByIsbn("1");
        Item savedItem = byIsbn.get();

        CartItem cartItem1 = CartItem.builder()
                .cart(cart)
                .item(savedItem)
                .count(2)
                .build();
        cart.getCartItems().add(cartItem1);
        Cart cart1 = cartRepository.save(cart);// 임시로 Cart를 생성 후 CartItem 까지 등록

        // When Command로 cartItem 등을 호출한 상황

        cart1.getId();
        // cartItem Data
        List<CartItem> cartItems = cart1.getCartItems();
        // 구매자는 위의 buyerId를 사용

        // Order를 위한 멱등성 키
        String idemKey = IdempotencyCreator.create(cart);

        // 주문 이름
        String orderName = cartItems.stream()
                .map(orderItem -> orderItem.getItem().getTitle())
                .collect(Collectors.joining(", "));

        PaymentEvent paymentEvent = PaymentEvent.builder()
                .buyerId(buyerId)
                .orderId(idemKey)
                .orderName(orderName)
                .method(PaymentMethod.EASY_PAY)
//                .paymentOrders(paymentOrders)
                .build();

        // then when
        List<PaymentOrder> paymentOrders = cartItems.stream()
                .map(cartItem -> PaymentOrder.builder()
                        .sellerId(cartItem.getItem().getSellerId())
                        .orderId(idemKey)
                        .productId(cartItem.getItem().getIsbn())
                        .amount(cartItem.getItem().getPrice() * cartItem.getCount())
                        .paymentStatus(PaymentStatus.NOT_STARTED)
                        .paymentEvent(paymentEvent)
                        .build()
                )
                .collect(Collectors.toList());

        paymentEvent.setPaymentOrders(paymentOrders);
        springDataJpaPaymentEventRepository.save(paymentEvent);

        // ✅ 검증 시작
        List<PaymentEvent> paymentEventList = springDataJpaPaymentEventRepository.findAll();

        List<PaymentOrder> paymentOrderList = springDataJpaPaymentOrderRepository.findAll();

        System.out.println(" paymentEventList Size : " + paymentEventList.size());

        System.out.println(" paymentOrderList Size : " + paymentOrderList.size());

    }

    //@Test
    void OrderSaveTest(){

    }

}