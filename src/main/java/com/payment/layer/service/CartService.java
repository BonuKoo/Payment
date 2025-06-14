package com.payment.layer.service;

import com.payment.domain.item.Cart;
import com.payment.layer.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;

    public void save(Cart cart){
        cartRepository.save(cart);
    }
}
