package com.payment.payment.adapter.in.web.request;

import com.payment.domain.item.CartItem;
import lombok.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequest {

    private Long cartId;            // 주문 Id
    private List<CartItem> cartItemIds;  //  주문 목록
    private Long buyerId;           // 구매자 Id
    private String seed;            // 시간

    public CheckoutRequest(Long cartId, List<CartItem> cartItemIds, Long buyerId) {
        this.cartId = cartId;
        this.cartItemIds = cartItemIds;
        this.buyerId = buyerId;
        this.seed = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toString();
    }
}
