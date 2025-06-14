package com.payment.payment.util;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class IdempotencyCreator {

    private IdempotencyCreator() {
        // 인스턴스화 방지
    }

    public static String create(Object data) {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }
        return UUID.nameUUIDFromBytes(data.toString().getBytes(StandardCharsets.UTF_8)).toString();
    }

}

// 내부에 멤버 필드, 상태 등이 없고 static 메서드 하나만 제공하므로
// ApplicationContext에 등록하지 않고 static 으로 메모리에서 호출한다.