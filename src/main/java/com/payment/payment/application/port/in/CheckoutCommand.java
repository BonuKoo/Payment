package com.payment.payment.application.port.in;

import com.payment.domain.item.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutCommand {

    private Long cartId;
    private Long buyerId;
    private List<CartItem> cartItemIds;
    private String idempotencyKey;

}
