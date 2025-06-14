package com.payment.listener;

import com.payment.domain.order.Order;
import com.payment.domain.order.OrderStatus;
import com.payment.domain.payment.PaymentEvent;
import com.payment.domain.payment.PaymentStatus;
import com.payment.layer.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventListener {

    private final OrderRepository orderRepository;

    /*
    @EventListener
    public void handlePaymentStatusUpdate(PaymentEvent paymentEvent) {
        Order order = paymentEvent.getOrder();

        if (paymentEvent.isSuccess()) {
            order.updateOrderStatus(OrderStatus.PAID);
            log.info("Order {} has been PAID", order.getId());
        } else if (paymentEvent.getPaymentStatus() == PaymentStatus.FAILURE) {
            order.updateOrderStatus(OrderStatus.CANCELED);
            log.info("Order {} has been CANCELED due to payment failure", order.getId());
        }

        orderRepository.save(order);
    }
*/

}
