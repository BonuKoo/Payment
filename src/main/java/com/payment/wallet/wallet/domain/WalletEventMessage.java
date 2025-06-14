package com.payment.wallet.wallet.domain;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class WalletEventMessage {

    private WalletEventMessageType type;
    private Map<String, Object> payload ; //new HashMap<>();
    private Map<String, Object> metadata ; //new HashMap<>();
}

