package com.wallet.ledger.ledger.application.port.in;

import com.wallet.ledger.ledger.domain.LedgerEventMessage;
import com.wallet.ledger.ledger.domain.PaymentEventMessage;

public interface DoubleLedgerEntryRecordUseCase {

    /**
     * 장부 기입을 처리하는 메서드.
     * 페이먼트 이벤트 메시지를 받아서 장부에 기입한 후, 카프카에 발행할 Ledger 이벤트 메세지를 반환
     */
    LedgerEventMessage recordDoubleLedgerEntry(PaymentEventMessage message);

}
