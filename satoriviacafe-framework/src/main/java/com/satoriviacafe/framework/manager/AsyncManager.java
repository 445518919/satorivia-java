package com.satoriviacafe.framework.manager;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalNotification;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 异步任务管理器（现代化写法，虚拟线程兼容，支持优雅关闭）
 */
@Slf4j
@Component
@SuppressWarnings("NullableProblems,unused")
public class AsyncManager {

    /**
     * 操作延迟10毫秒（預設短延遲）
     */
    private static final long OPERATE_DELAY_TIME = 10;


    /**
     * 异步任务调度线程池
     */
    private final ScheduledExecutorService executor;

    /**
     * 默认防抖窗口（毫秒），当调用方未传入时使用
     */
    private final long defaultDebounceTime;

    /**
     * Guava Cache：key -> ScheduledFuture
     * 使用 maximumSize + expireAfterWrite 来限制内存增长
     */
    private final  Cache<Object, ScheduledFuture<?>> debounceCache;

    public AsyncManager(
            @Qualifier("scheduledExecutorService") ScheduledExecutorService executor,
            @Value("${com.async.debounce-max-size:10000}") long debounceMaxSize,
            @Value("${com.async.debounce-expire-millis:5000}") long debounceExpireMillis,
            @Value("${com.async.default-debounce-time:300}") long defaultDebounceTime
    ) {
        this.executor = executor;
        this.defaultDebounceTime = defaultDebounceTime;

        // 建立 cache，當 entry 被移除時（過期/容量/手動 invalidation），取消對應的 future（如果尚未完成）。
        this.debounceCache = CacheBuilder.newBuilder()
                .maximumSize(Math.max(1, debounceMaxSize))
                .expireAfterWrite(Math.max(1, debounceExpireMillis), TimeUnit.MILLISECONDS)
                .removalListener((RemovalNotification<Object, ScheduledFuture<?>> notif) -> {
                    ScheduledFuture<?> f = notif.getValue();
                    if (f != null && !f.isDone()) {
                        try {
                            f.cancel(false);
                        } catch (Throwable ex) {
                            log.warn("取消被移除的 debounce future 失败 key={}, cause={}", notif.getKey(), ex.toString());
                        }
                    }
                })
                .build();
    }

    /**
     * 执行异步任务
     * @param task 要执行的任务
     * @param delayMillis 延迟时间（毫秒），若为0则立即执行
     */

    public void execute(Runnable task, long delayMillis) {
        executor.schedule(task, delayMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * 执行异步任务，使用默认延迟时间（10毫秒）
     * @param task 要执行的任务
     */
    public void executeDelay(Runnable task) {
        execute(task, OPERATE_DELAY_TIME);
    }

    /**
     * 执行异步任务，立即执行
     * @param task 要执行的任务
     */
    public void execute(Runnable task) {
        execute(task, 0);
    }

    /**
     * 以防抖方式执行任务（同一 key 的重复调度会取消前一个未执行的任务）
     * @param key 防抖键（同一 key 会被合并，建议使用带命名空间的键，例如 "comment:123"）
     * @param delayMillis 防抖窗口（毫秒），如果 <=0 则使用默认值
     * @param task 要执行的任务
     */
    public void debounceExecute(Object key, long delayMillis, Runnable task) {
        scheduleDebounced(key, delayMillis, task);
    }

    /**
     * 使用默认防抖时间
     * @param key 防抖键
     * @param task 要执行的任务
     */
    public void debounceExecute(Object key, Runnable task) {
        debounceExecute(key, this.defaultDebounceTime, task);
    }

    /**
     * 在事务提交后执行任务（若当前无事务则立即执行）
     *
     * @param task 要执行的任务
     */
    public void executeAfterCommit(Runnable task) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    try {
                        executeDelay(() -> {
                            try {
                                task.run();
                            } catch (Throwable ex) {
                                log.error("executeAfterCommit 執行任務失敗", ex);
                            }
                        });
                    } catch (Throwable t) {
                        log.error("executeAfterCommit 排程任務失敗", t);
                    }
                }
                @Override public void beforeCommit(boolean readOnly) {}
                @Override public void beforeCompletion() {}
                @Override public void afterCompletion(int status) {}
                @Override public void suspend() {}
                @Override public void resume() {}
                @Override public void flush() {}
            });
        } else {
            executeDelay(() -> {
                try {
                    task.run();
                } catch (Throwable ex) {
                    log.error("executeAfterCommit (no tx) 執行任務失敗", ex);
                }
            });
        }
    }

    /**
     * 在事务提交后以防抖方式排程任务（同一 key 的重复调度会取消前一个未执行的任务）
     *
     * @param key         防抖键（同一 key 会被合并，建议使用带命名空间的键，例如 "comment:123"）
     * @param delayMillis 防抖窗口（毫秒），如果 <=0 则使用默认值
     * @param task        要执行的任务
     */
    public void debounceAfterCommit(Object key, long delayMillis, Runnable task) {
        Objects.requireNonNull(key, "debounce key cannot be null");
        final long finalDelayMillis = delayMillis > 0 ? delayMillis : this.defaultDebounceTime;

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    scheduleDebounced(key, finalDelayMillis, task);
                }
                @Override public void beforeCommit(boolean readOnly) {}
                @Override public void beforeCompletion() {}
                @Override public void afterCompletion(int status) {}
                @Override public void suspend() {}
                @Override public void resume() {}
                @Override public void flush() {}
            });
        } else {
            scheduleDebounced(key, finalDelayMillis, task);
        }
    }

    /**
     * 使用默认防抖时间
     */
    public void debounceAfterCommit(Object key, Runnable task) {
        debounceAfterCommit(key, this.defaultDebounceTime, task);
    }

    /**
     * 排程防抖任务（不考虑事务）
     * @param key 防抖键
     * @param delayMillis 防抖窗口（毫秒）
     * @param task 要执行的任务
     */
    private void scheduleDebounced(Object key, long delayMillis, Runnable task) {
        // holder 用來讓 wrapped runnable 能參考到對應的 future
        final ScheduledFuture<?>[] holder = new ScheduledFuture<?>[1];

        Runnable wrapped = () -> {
            try {
                task.run();
            } catch (Throwable ex) {
                log.error("debounced task failed for key {}", key, ex);
            } finally {
                ScheduledFuture<?> f = holder[0];
                if (f != null) {
                    // 只删除与本次 future 相同的缓存项，避免误删除被替换的新任务
                    debounceCache.asMap().remove(key, f);
                }
            }
        };

        // 取消旧任务（若存在）
        ScheduledFuture<?> previous = debounceCache.getIfPresent(key);
        if (previous != null && !previous.isDone()) {
            try {
                previous.cancel(false);
            } catch (Throwable ex) {
                log.warn("取消旧 debounce future 失败 key={}, err={}", key, ex.toString());
            }
        }

        // 排程新的任务并放入 cache
        ScheduledFuture<?> future = executor.schedule(wrapped, delayMillis, TimeUnit.MILLISECONDS);
        holder[0] = future;
        debounceCache.put(key, future);
    }

    /**
     * 取消特定 key 的防抖任务（若存在）
     */
    public void cancelDebounce(Object key) {
        ScheduledFuture<?> f = debounceCache.asMap().remove(key);
        if (f != null && !f.isDone()) {
            try {
                f.cancel(false);
            } catch (Throwable ex) {
                log.warn("cancelDebounce 取消任务失败 key={}, err={}", key, ex.toString());
            }
        }
    }

    @PreDestroy
    public void shutdown() {
        log.info("关闭异步任务调度线程池...");
        try {
            // 通过 invalidateAll 触发 RemovalListener，自动取消将要执行的 futures
            debounceCache.invalidateAll();
        } catch (Throwable ex) {
            log.warn("清理 debounce cache 时发生錯誤", ex);
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                log.warn("异步任务线程池未正常关闭，强制关闭");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            log.error("关闭异步任务线程池被中断", e);
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
