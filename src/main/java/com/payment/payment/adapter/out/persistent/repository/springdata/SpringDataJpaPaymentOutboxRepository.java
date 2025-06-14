package com.payment.payment.adapter.out.persistent.repository.springdata;

import com.payment.domain.outbox.Outbox;
import com.payment.payment.adapter.out.persistent.repository.querydsl.PaymentOutboxRepository4Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataJpaPaymentOutboxRepository extends JpaRepository<Outbox,Long>, PaymentOutboxRepository4Query {

    Optional<Outbox> findByIdempotencyKey(String key);

    /*
    @Modifying
    @Query("UPDATE Outbox o SET o.status = :status WHERE o.idempotencyKey = :key AND o.type = :type")
    void updateStatus(String key, String type, OutboxStatus status);


    */

    // 전송되지 않았거나 (INIT), 전송 실패 (FAILURE)했던 재시도 대상 메시지 목록을 조회
    //@Query("SELECT o FROM Outbox o WHERE (o.status = 'INIT' OR o.status = 'FAILURE') AND o.createdAt <= :cutoff AND o.type = :type")
}
