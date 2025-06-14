package com.wallet.ledger.ledger.application.port.out;

import com.wallet.ledger.ledger.domain.PaymentEventMessage;

public interface DuplicateMessageFilterPort {

    Boolean isAlreadyProcess(PaymentEventMessage message);

}
