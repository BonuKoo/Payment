package com.payment.wallet.wallet.application.service;


import com.payment.wallet.wallet.application.port.in.SettlementUseCase;
import com.payment.wallet.wallet.application.port.out.*;
import com.payment.wallet.wallet.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SettlementService implements SettlementUseCase {

    private final DuplicateMessageFilterPort duplicateMessageFilterPort;
    private final LoadPaymentOrderPort loadPaymentOrderPort;
    private final LoadWalletPort loadWalletPort;
    private final SaveWalletPort saveWalletPort;
    private final UpdateWalletPort updateWalletPort;

    @Override
    @Transactional
    public WalletEventMessage processSettlement(PaymentEventMessage paymentEventMessage) {
        if (duplicateMessageFilterPort.isAlreadyProcess(paymentEventMessage)){

            //System.out.println("받은 orderId: {} " + paymentEventMessage.getOrderId());

            return createWalletEventMessage(paymentEventMessage);
        }
        /** orderId 확인
         * */

        System.out.println("받은 orderId: {} " + paymentEventMessage.getOrderId());

        List<PaymentOrderDTO> paymentOrders = loadPaymentOrderPort.getPaymentOrders(paymentEventMessage.getOrderId());

        /** 정산 대상이 실제로 불러와졌는가 확인
         * 이후 제거 */
        System.out.println("[DEBUG] 불러온 결제 주문 수: " + paymentOrders.size());
        paymentOrders.forEach(p -> System.out.println("[DEBUG] paymentOrder: userId=" + p.getSellerId() + ", sellerId=" + p.getSellerId() + ", amount=" + p.getAmount()));

        Map<Long, List<PaymentOrderDTO>> paymentOrdersBySellerId = paymentOrders.stream()
                .collect(Collectors.groupingBy(PaymentOrderDTO::getSellerId));

        List<WalletDTO> updatedWallets = getUpdatedWallets(paymentOrdersBySellerId);


        /** 데이터 만들어졌는 지 확인
         * 이후 제거 */
        System.out.println("[DEBUG] 저장 직전 지갑 상태:");
        updatedWallets.forEach(w -> System.out.println("userId=" + w.getUserId() + ", balance=" + w.getBalance()));
        saveWalletPort.save(updatedWallets);

        // 2.
        updateWalletPort.update(updatedWallets);

        return createWalletEventMessage(paymentEventMessage);

        //throw new IllegalStateException("결제 메시지를 아직 처리하지 않았습니다.");
    }

    private WalletEventMessage createWalletEventMessage(PaymentEventMessage paymentEventMessage){

        Map<String, Object> payload = new HashMap<>();
        payload.put("orderId", paymentEventMessage.getOrderId());

        return WalletEventMessage.builder()
                .type(WalletEventMessageType.SUCCESS)
                .payload(payload)
                .build();
    }

    private List<WalletDTO> getUpdatedWallets(Map<Long , List<PaymentOrderDTO>> paymentOrdersBySellerId) {
        Set<Long> sellerIds = paymentOrdersBySellerId.keySet();

        Set<WalletDTO> wallets = loadWalletPort.getWallets(sellerIds);
        /** 정산 대상 지갑이 실제로 존재하는 지 확인
         * 이후 제거 */
        System.out.println("[DEBUG] 불러온 지갑 수: " + wallets.size());
        wallets.forEach(w -> System.out.println("[DEBUG] wallet: userId=" + w.getUserId() + ", balance=" + w.getBalance()));
        return wallets.stream()
                .map(walletDTO -> {
                    List<PaymentOrderDTO> orders = paymentOrdersBySellerId.get(walletDTO.getUserId());
                    if (orders == null || orders.isEmpty()) {
                        /**  실제로 금액이 업데이트되었는지 확인   이후 제거 */
                        System.out.println("[DEBUG] 해당 유저에 대한 주문 없음: userId=" + walletDTO.getUserId());

                        return walletDTO; // 혹은 필요시 예외 처리 또는 원본 그대로 유지
                    }

                    // calculateBalanceWith(List<Item>) 메서드는 PaymentOrderDTO도 허용함
                    WalletDTO updated = walletDTO.calculateBalanceWith(new ArrayList<>(orders));
                    System.out.println("[DEBUG] 업데이트된 지갑: userId=" + updated.getUserId() + ", 새로운 잔액=" + updated.getBalance());
                    return updated;
                })
                .toList();

    }


}

/**
 Duplicate Message Filter Port Interface를 만들어서 중복 메시지 방지 처리
 */

/**
 * 정산 처리를 완료한 이후 갑작스럽게 어플이 Crash가 발생해서
 * Wallet Event Message 발행에 실패할 가능성이 있기 때문에
 * 이미 처리된 결제 이벤트임에도 Event Message를 반환
 *
 * 발행이 실패할 경우를 대비해서, 이미 처리한 이벤트라도 Wallet 메시지를 한 번 더 발행
 */