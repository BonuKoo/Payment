package com.payment.layer.repository;

import com.payment.domain.item.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    //@Test
    void AddItem(){

        Item newItem = Item.builder()
                .isbn("1")
                .title("임시")
                .price(35000)
                .stockQuantity(9999)
                .build();

        itemRepository.save(newItem);
    }

}