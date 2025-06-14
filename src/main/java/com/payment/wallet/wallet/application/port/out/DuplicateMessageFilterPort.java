package com.payment.wallet.wallet.application.port.out;

import com.payment.wallet.wallet.domain.PaymentEventMessage;

public interface DuplicateMessageFilterPort {

    Boolean isAlreadyProcess(PaymentEventMessage paymentEventMessage);

}

/**
    정산이 이미 처리되었는 지 여부를 판단
    이미 처리되었다면, WalletEventMessage를 반환
 */