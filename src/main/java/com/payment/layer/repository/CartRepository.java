package com.payment.layer.repository;

import com.payment.domain.item.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository  extends JpaRepository<Cart, Long> {

    @EntityGraph(attributePaths = {"cartItems", "cartItems.item"})
    Optional<Cart> findById(Long id);

    Cart findByBuyerId(Long buyerId);

}
