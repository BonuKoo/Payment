package com.payment.domain.dto;

import com.payment.domain.item.Cart;
import com.payment.domain.item.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter
public class CartItemUpdateRequestForm {

    private String account_Id;
    private Item item;
    private Cart cart;
    private int amount;

}
