package com.payment.payment.application.service;

import com.payment.domain.item.Cart;
import com.payment.domain.item.CartItem;
import com.payment.domain.item.Item;
import com.payment.domain.payment.PSPConfirmationStatus;
import com.payment.domain.payment.PaymentMethod;
import com.payment.domain.payment.PaymentStatus;
import com.payment.domain.payment.PaymentType;
import com.payment.layer.repository.CartRepository;
import com.payment.layer.repository.ItemRepository;
import com.payment.payment.adapter.out.persistent.repository.springdata.SpringDataJpaPaymentEventRepository;
import com.payment.payment.adapter.out.web.toss.executor.TossPaymentExecutor;
import com.payment.payment.application.port.in.CheckoutCommand;
import com.payment.payment.application.port.in.PaymentConfirmCommand;
import com.payment.payment.domain.CheckoutResult;
import com.payment.payment.domain.PaymentConfirmationResult;
import com.payment.payment.domain.PaymentExecutionResult;
import com.payment.payment.domain.PaymentExtraDetails;
import com.payment.payment.util.IdempotencyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

//@SpringBootTest
@AutoConfigureMockMvc
@Import(TossPaymentExecutor.class)
class PaymentConfirmServiceTest {

    @Autowired
    private SpringDataJpaPaymentEventRepository eventRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private PaymentConfirmService confirmService;

    @MockBean
    private TossPaymentExecutor tossPaymentExecutor;

    //@BeforeEach
    void cleanUp() {
        cartRepository.deleteAll();
        itemRepository.deleteAll();
//        eventRepository.deleteAll();
    }

    /*
    @Autowired
    private PaymentStatusUpdatePort paymentStatusUpdatePort;
    */
    //@Test
    void shouldMarkAsSuccess_whenPaymentConfirmationSucceeds() {
        // given
        Long buyerId = 11L;

        Item item = Item.builder()
                .isbn("3")
                .title("테스트 상품3")
                .price(10000)
                .stockQuantity(10)
                .sellerId(5L)
                .build();
        itemRepository.save(item);

        Cart cart = Cart.builder().buyerId(buyerId).build();

        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .item(item)
                .count(2)
                .build();
        cart.getCartItems().add(cartItem);

        cartRepository.save(cart);

        // orderId
        String idempotencyKey = IdempotencyCreator.create(cart);

        Cart savedCart = cartRepository.findByBuyerId(11L);

        List<CartItem> cartItems = savedCart.getCartItems();

        // when
        CheckoutCommand checkoutCommand = new CheckoutCommand(
                savedCart.getId(), buyerId, cartItems, idempotencyKey
        );

        CheckoutResult checkoutResult = checkoutService.checkout(checkoutCommand);

        PaymentConfirmCommand paymentConfirmCommand = new PaymentConfirmCommand(
                UUID.randomUUID().toString(),
                checkoutResult.getOrderId(),
                checkoutResult.getAmount()
        );

        PaymentExecutionResult mockResult = new PaymentExecutionResult(
                paymentConfirmCommand.getPaymentKey(),
                paymentConfirmCommand.getOrderId(),
                PaymentExtraDetails.builder()
                        .type(PaymentType.NORMAL)
                        .method(PaymentMethod.EASY_PAY)
                        .totalAmount(paymentConfirmCommand.getAmount())
                        .orderName("test_order_name")
                        .pspConfirmationStatus(PSPConfirmationStatus.DONE)
                        .approvedAt(LocalDateTime.now())
                        .pspRawData("{}")
                        .build(),
                null,             // failure - 성공이므로 null
                true,             // isSuccess
                false,            // isFailure
                false,            // isUnknown
                false             // isRetryable
        );

        when(tossPaymentExecutor.execute(paymentConfirmCommand)).thenReturn(mockResult);

        // then
        PaymentConfirmationResult result = confirmService.confirm(paymentConfirmCommand);
        assertThat(result.getStatus()).isEqualTo(PaymentStatus.SUCCESS);


    }
}