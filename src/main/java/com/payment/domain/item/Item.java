package com.payment.domain.item;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Getter @Builder
public class Item {

    @Id @Column(name = "isbn",unique = true)
    private String isbn;

    private String title;

    private int price;

    private int stockQuantity;
    // 판매자 - 임시
    private String sellerId;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    //Item 등록 생성자
    public Item(String isbn, String title, int price, int stockQuantity) {
        this.isbn = isbn;
        this.title = title;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    //재고 추가
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    //재고 줄어듬
    public void removeStock(int quantity) {

        if (this.stockQuantity - quantity < 0) {
            throw new RuntimeException("재고는 0개 미만이 될 수 없습니다.");
        }

        this.stockQuantity -= quantity;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void addCartItem(CartItem cartItem) {
        this.cartItems.add(cartItem);
        cartItem.setItem(this);
    }

    public void removeCartItemFromItem(CartItem cartItem) {
        this.cartItems.remove(cartItem);
        cartItem.setItem(null);
    }

}
