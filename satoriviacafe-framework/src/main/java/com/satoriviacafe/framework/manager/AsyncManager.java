package com.satoriviacafe.framework.manager;

import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.function.Supplier;

import static com.satoriviacafe.common.utils.TryUtils.tryRun;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * 异步任务管理器（现代化写法，虚拟线程兼容，支持优雅关闭）
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Getter
public class AsyncManager {

    private final ScheduledExecutorService scheduledExecutor;
    private final ExecutorService executor;
    private @Value("${app.async.schedule-delay-ms:100}") long scheduleDelayMs;
    private @Value("${app.async.default-cd-ms:5000}") long defaultCdMs;

    /**
     * 执行异步任务
     */
    public @NotNull CompletableFuture<Void> runAsync(@NotNull Runnable runnable) {
        return CompletableFuture.runAsync(runnable, executor);
    }

    /**
     * 执行异步任务
     */
    public <R> @NotNull CompletableFuture<R> supplyAsync(@NotNull Supplier<R> supplier) {
        return CompletableFuture.supplyAsync(supplier, executor);
    }

    /**
     * 执行异步任务
     */
    public void execute(final Runnable task) {
        executor.execute(() -> tryRun(task, "AsyncManager 任务执行异常"));
    }

    /**
     * 执行异步任务
     *
     * @param errMsg 任务执行异常时的错误信息
     */
    public void execute(String errMsg, final Runnable task) {
        executor.execute(() -> tryRun(task, errMsg));
    }

    /**
     * 提交异步任务
     */
    public <T> Future<? extends @Nullable T> submit(Callable<? extends @Nullable T> task) {
        return executor.submit(() -> tryRun(task, "submit 异步任务执行异常"));
    }


    /**
     * 以指定延迟调度任务执行
     */
    public void schedule(final long delayMillis, @NonNull final Runnable task) {
        // 建议包裹，否则延迟任务报错即“失踪”
        scheduledExecutor.schedule(() -> tryRun(task, "异步延迟任务执行失败"), delayMillis, MILLISECONDS);
    }

    /**
     * 以默认延迟调度任务执行
     *
     * @param task 任务
     */
    public void schedule(final Runnable task) {
        scheduledExecutor.schedule(() -> tryRun(task, "异步延迟任务执行失败"), scheduleDelayMs, MILLISECONDS);
    }


    /**
     * 以固定频率调度任务执行
     */
    public void scheduleAtFixedRate(long initialDelay, Duration period, @NotNull Runnable task) {
        scheduledExecutor.scheduleAtFixedRate(() -> tryRun(task, "定时任务执行失败"), initialDelay, period.toNanos(), NANOSECONDS);
    }


    // =========== 优雅关闭 ==========

    @PreDestroy
    public void shutdown() {
        log.info("关闭异步任务调度线程池...");
        executor.shutdown();
        scheduledExecutor.shutdown();
        try {
            if (!scheduledExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                log.warn("异步任务线程池未正常关闭，强制关闭");
                executor.shutdownNow();
                scheduledExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            log.error("关闭异步任务线程池被中断", e);
            executor.shutdownNow(); // 修正为 shutdownNow
            scheduledExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public void executeDelay(Runnable runnable) {
        scheduledExecutor.schedule(() -> tryRun(runnable, "异步延迟任务执行失败"), scheduleDelayMs, MILLISECONDS);
    }
}
