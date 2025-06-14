package com.payment.payment.application.service;

import com.payment.domain.item.CartItem;
import com.payment.domain.payment.PaymentEvent;
import com.payment.domain.payment.PaymentOrder;
import com.payment.domain.payment.PaymentStatus;
import com.payment.layer.repository.CartRepository;
import com.payment.payment.adapter.out.persistent.repository.springdata.SpringDataJpaPaymentEventRepository;
import com.payment.payment.application.port.in.CheckoutCommand;
import com.payment.payment.application.port.in.CheckoutUseCase;
import com.payment.payment.application.port.out.SavePaymentPort;
import com.payment.payment.domain.CheckoutResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheckoutService implements CheckoutUseCase {

    // @todo 추후 Repository를 헥사고날로 수정 할 지 고민
    //    private final LoadOrderPort loadProductPort;
    //    private final SavePaymentPort savePaymentPort;
    private final CartRepository cartRepository;
    private final SavePaymentPort savePaymentPort;
    private final SpringDataJpaPaymentEventRepository springDataJpaPaymentEventRepository;

    /*
    public CheckoutService(LoadOrderPort loadProductPort, SavePaymentPort savePaymentPort) {
        this.loadProductPort = loadProductPort;
        this.savePaymentPort = savePaymentPort;
    }*/

    @Override
    public CheckoutResult checkout(CheckoutCommand command) {
        //  Command를 통해 Cart data를 가져온다.

        // Checkout시, 컨트롤러에서 넘어 온 CartId와, cart에 담긴 Item id들
        Long cartId = command.getCartId();
        List<CartItem> cartItemIds = command.getCartItemIds();

        PaymentEvent paymentEvent = createPaymentEvent(command, cartItemIds);

        // PaymentEvent 만드는 과정 -> 현재는 엔티티로 바로 저장 중. 리팩토링 필요
        springDataJpaPaymentEventRepository.save(paymentEvent);

        return new CheckoutResult(
                paymentEvent.totalAmount(),
                paymentEvent.getOrderId(),
                paymentEvent.getOrderName()
        );
    }

    private PaymentEvent createPaymentEvent(CheckoutCommand command, List<CartItem> cartItems) {

        // 오류 발생 시 CartRepository의 findById의 EntityGraph를 참고

        List<PaymentOrder> paymentOrders = cartItems.stream()
                .map(cartItem -> PaymentOrder.builder()
                        .sellerId(cartItem.getItem().getSellerId())
                        .orderId(command.getIdempotencyKey())
                        .productId(cartItem.getItem().getIsbn())
                        .amount(cartItem.getItem().getPrice() * cartItem.getCount())
                        .paymentStatus(PaymentStatus.NOT_STARTED)
                        .build()
                )
                .collect(Collectors.toList());

        // 주문 이름
        String orderName = cartItems.stream()
                .map(orderItem -> orderItem.getItem().getTitle())
                .collect(Collectors.joining(", "));

        PaymentEvent build = PaymentEvent.builder()
                .buyerId(command.getBuyerId())
                .orderId(command.getIdempotencyKey())
                .orderName(orderName)
                .paymentOrders(paymentOrders)
                .build();

        for (PaymentOrder order : paymentOrders) {
            order.setPaymentEvent(build);
        }

        return build;
    }
}