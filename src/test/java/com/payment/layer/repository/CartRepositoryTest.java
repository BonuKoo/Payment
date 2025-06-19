package com.payment.layer.repository;

import com.payment.domain.item.Cart;
import com.payment.domain.item.CartItem;
import com.payment.domain.item.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class CartRepositoryTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CartItemRepository cartItemRepository;

//    @Test
    void findById_WithEntityGraph_ShouldFetchCartItemsAndItem() {

        // Given: Cart, Item, CartItem 생성 및 저장
        Item item = Item.builder()
                .isbn("ISBN-1236")
                .title("Test_Item3")
                .price(20000)
                .stockQuantity(100)
                .sellerId(3L)
                .build();
        itemRepository.save(item);

        Cart cart = Cart.builder()
                .buyerId(4L)
                .build();
        cartRepository.save(cart);

        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .item(item)
                .count(3)
                .build();
        cartItemRepository.save(cartItem);

    }

//    @Test
    @Transactional
    void findAll(){
        List<Cart> cartList = cartRepository.findAll();

        System.out.println("카트 개수: " + cartList.size());


        for (int i = 0; i < cartList.size(); i++) {
            Cart cart = cartList.get(i);
            System.out.println("Cart Index : " + i);
            System.out.println("Cart Id : " + cart.getId());
            System.out.println("Cart Id : " + cart.getBuyerId());

            List<CartItem> cartItems = cart.getCartItems();

            System.out.println("CartItems:");
            for (CartItem item : cartItems) {
                System.out.println(" - Item ID: " + item.getId());
                System.out.println("   Item ISBN: " + item.getItem().getIsbn());
                System.out.println("   Count: " + item.getCount());
            }

            System.out.println("-------------------------------");
        }

}
}