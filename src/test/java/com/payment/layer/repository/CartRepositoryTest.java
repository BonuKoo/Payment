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

@SpringBootTest
//@Transactional
class CartRepositoryTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Test
    void findById_WithEntityGraph_ShouldFetchCartItemsAndItem() {

        // Given: Cart, Item, CartItem 생성 및 저장
        Item item = Item.builder()
                .isbn("ISBN-1235")
                .title("Test_Item2")
                .price(20000)
                .stockQuantity(100)
                .sellerId("SellerTest")
                .build();
        itemRepository.save(item);

        Cart cart = Cart.builder()
                .buyerId(1L)
                .build();
        cartRepository.save(cart);

        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .item(item)
                .count(3)
                .build();
        cartItemRepository.save(cartItem);
        /*
        //Long cartId = cart.getId();
        Cart cartOpt = cartRepository.findById(0L).get();
        Long cartId = cartOpt.getId();
        // When: findById 호출
        Optional<Cart> optionalCart = cartRepository.findById(cartId);

        // Then: EntityGraph에 의해 cartItems와 item이 모두 로딩되었는지 확인

        assertThat(optionalCart).isPresent();
        Cart foundCart = optionalCart.get();

        // cartItems 검증
        assertThat(foundCart.getCartItems()).hasSize(1);

        CartItem foundCartItem = foundCart.getCartItems().get(0);
        assertThat(foundCartItem.getCount()).isEqualTo(2);

        // 아무튼 가져온다는 사실 확인
        assertThat(foundCartItem.getItem()).isNotNull();
        assertThat(foundCartItem.getItem().getTitle()).isEqualTo("테스트 상품");
         */
    //*/
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