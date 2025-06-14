package com.payment.payment.adapter.out.web.product.client;

import com.payment.domain.item.CartItem;
import com.payment.domain.order.OrderItem;

import java.util.List;

public interface OrderItemClient {

    List<OrderItem> getCartItems(Long orderId, List<Long> orderItemIds);

}
