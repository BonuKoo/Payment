package com.payment.layer.service;

import com.payment.domain.dto.CartItemUpdateRequestForm;
import com.payment.domain.item.Cart;
import com.payment.domain.item.CartItem;
import com.payment.domain.item.Item;
import com.payment.layer.repository.CartItemRepository;
import com.payment.layer.repository.CartRepository;
import com.payment.layer.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
@Transactional
class CartItemServiceTest {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    //@Test
    void 장바구니_아이템_추가_테스트() {
        // Given (기본 데이터 생성)

        String account_Id = "임시";

        Cart cart = new Cart();
        cartRepository.save(cart);

        Item item = new Item("123456", "테스트 상품", 10000, 10);
        itemRepository.save(item);

        CartItemUpdateRequestForm form = new CartItemUpdateRequestForm(account_Id,item,cart,2);

        // When (장바구니에 아이템 추가)
        cartItemService.createCartItemOrIncreaseAmount(form);

        // Then (장바구니가 정상적으로 추가되었는지 검증)
        List<CartItem> cartItems = cartItemRepository.findAll();
        assertThat(cartItems).hasSize(1);
        assertThat(cartItems.get(0).getCount()).isEqualTo(2);
    }

}