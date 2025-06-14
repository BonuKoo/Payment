package com.payment.layer.service;

import com.payment.domain.dto.CartItemUpdateRequestForm;
import com.payment.domain.item.Cart;
import com.payment.domain.item.CartItem;
import com.payment.domain.item.Item;
import com.payment.layer.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartService cartService;

    public void createCartItemOrIncreaseAmount(CartItemUpdateRequestForm form){

        Optional<CartItem> existingCartItemOpt = cartItemRepository.findByCartAndItem(form.getCart(), form.getItem());

        if (existingCartItemOpt.isPresent()){
            CartItem cartItem = existingCartItemOpt.get();
            cartItem.addCount(form.getAmount());

            cartItemRepository.save(cartItem);

        }else {

            CartItem cartItem = CartItem.builder()
                    .cart(form.getCart())
                    .item(form.getItem())
                    .count(form.getAmount())
                    .build();

            Cart cart = form.getCart();
            int amount = form.getAmount();
            Item item = form.getItem();

            cartItemRepository.save(cartItem);

        }
    }

    public void saveCartItem(CartItem cartItem) {

        //cartItem을 먼저 저장.
        cartItemRepository.save(cartItem);

        //cart가 아직 저장되지 않은 경우 저장
        Cart cart = cartItem.getCart();
        if (cart.getId() == null){
            cartService.save(cart);
        }
    }

}
