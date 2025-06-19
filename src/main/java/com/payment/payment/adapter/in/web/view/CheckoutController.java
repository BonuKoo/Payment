package com.payment.payment.adapter.in.web.view;

import com.payment.domain.item.Cart;
import com.payment.domain.item.CartItem;
import com.payment.layer.repository.CartRepository;
import com.payment.payment.adapter.in.web.request.CheckoutRequest;
import com.payment.payment.application.port.in.CheckoutCommand;
import com.payment.payment.application.port.in.CheckoutUseCase;
import com.payment.payment.domain.CheckoutResult;
import com.payment.payment.util.IdempotencyCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class CheckoutController {

    private final CartRepository cartRepository;
    private final CheckoutUseCase checkoutUseCase;

    // 1. 사전 결제 확인
    @GetMapping("/checkout")
    public String checkoutPage(Model model){

        // Cart , CartItem
        Cart cart = cartRepository.findById(4L)
                .orElseThrow(() -> new IllegalArgumentException("Cart 없다"));
        List<CartItem> cartItems = cart.getCartItems();

        //총 주문 금액 임시
        int totalAmount = cartItems.stream()
                .mapToInt(cartItem ->
                        cartItem.getItem().getPrice() * cartItem.getCount())
                .sum();

        // 주문 이름 임시
        model.addAttribute("cartId", cart.getId());
        model.addAttribute("buyerId",cart.getBuyerId());
        model.addAttribute("orderId", UUID.randomUUID().toString());
        model.addAttribute("orderName", "테스트_주문");
        model.addAttribute("cartItemIds", cartItems.stream()
                .map(CartItem::getId)
                .collect(Collectors.toList()));
        model.addAttribute("amount", totalAmount);

        return "checkout";
    }

    @PostMapping("/checkout")
    public String  checkout(@ModelAttribute
                           CheckoutRequest request, RedirectAttributes redirectAttributes) {

        // 프론트에서 장바구니 정보 및 배송 정보 등이 입력.
        CheckoutCommand checkoutCommand = CheckoutCommand.builder()
                .cartId(request.getCartId())          // 기존엔 Cart 였음
                .buyerId(request.getBuyerId())
                .cartItemIds(request.getCartItemIds())
                .idempotencyKey(IdempotencyCreator.create(request))
                .build();

        CheckoutResult result = checkoutUseCase.checkout(checkoutCommand);

        redirectAttributes.addFlashAttribute("orderId", result.getOrderId());
        redirectAttributes.addFlashAttribute("orderName", result.getOrderName());
        redirectAttributes.addFlashAttribute("amount", result.getAmount());
        redirectAttributes.addFlashAttribute("buyerId", request.getBuyerId());

        return "redirect:/pay";
    }

    // 3. 결제 진행 페이지
    @GetMapping("/pay")
    public String payPage() {
        return "pay";
    }
}

/**
 * Cart -> Checkout -> Order -> Payment ( PaymentEvent & PaymentOrder )
 * 장바구니 -> 사용자가 결제를 준비하는 단계 -> 실제 저장하는 주문 계약 데이터.
 */
