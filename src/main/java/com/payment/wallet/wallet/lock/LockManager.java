package com.payment.wallet.wallet.lock;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Component
public class LockManager {

    private static class TimedLock {
        private final ReentrantLock lock = new ReentrantLock();
        private volatile long lastAccessTime = System.currentTimeMillis();

        public ReentrantLock getLock() {
            lastAccessTime = System.currentTimeMillis();
            return lock;
        }

        public long getLastAccessTime() {
            return lastAccessTime;
        }

    }

    private final ConcurrentHashMap<Long, TimedLock> lockMap = new ConcurrentHashMap<>();

    private static final long CLEANUP_INTERVAL_MS = 10 * 60 * 1000; // 10분
    private static final long LOCK_EXPIRATION_MS = 15 * 60 * 1000; // 15분
    private static final long LOCK_TIMEOUT_MS = 3000; // 3초 이내에 락 못 잡으면 실패 처리

    public LockManager() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            long now = System.currentTimeMillis();
            for (Map.Entry<Long, TimedLock> entry : lockMap.entrySet()) {
                TimedLock timedLock = entry.getValue();
                if (now - timedLock.getLastAccessTime() > LOCK_EXPIRATION_MS &&
                        !timedLock.getLock().isLocked()) {
                    lockMap.remove(entry.getKey());
                }
            }
        }, CLEANUP_INTERVAL_MS, CLEANUP_INTERVAL_MS, TimeUnit.MILLISECONDS);
    }

    /**
     * 여러 개의 락을 시도해서 획득. 3초 내에 하나라도 실패하면 모두 해제하고 예외 발생
     */
    public List<ReentrantLock> acquireLocks(List<Long> ids) {
        List<ReentrantLock> locks = ids.stream()
                .distinct()
                .sorted()
                .map(id -> lockMap.computeIfAbsent(id, key -> new TimedLock()).getLock())
                .collect(Collectors.toList());

        List<ReentrantLock> acquired = new CopyOnWriteArrayList<>();

        try {
            for (ReentrantLock lock : locks) {
                boolean success = lock.tryLock(LOCK_TIMEOUT_MS, TimeUnit.MILLISECONDS);
                if (!success) {
                    throw new TimeoutException("Lock acquisition timed out");
                }
                acquired.add(lock);
            }
            return acquired;
        } catch (Exception e) {
            // 이미 잡은 락 해제
            releaseLocks(acquired);
            throw new RuntimeException("Failed to acquire all locks", e);
        }
    }

    public void releaseLocks(List<ReentrantLock> locks) {
        for (int i = locks.size() - 1; i >= 0; i--) {
            ReentrantLock lock = locks.get(i);
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

}