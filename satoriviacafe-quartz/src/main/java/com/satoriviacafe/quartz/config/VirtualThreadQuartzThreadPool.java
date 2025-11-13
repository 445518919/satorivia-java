package com.satoriviacafe.quartz.config;

import lombok.Data;
import org.quartz.spi.ThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Data
@SuppressWarnings("unused")
public class VirtualThreadQuartzThreadPool implements ThreadPool {

    /**
     * 最大并发虚拟线程数
     */
    private int threadCount = 40;

    /**
     * 底层的虚拟线程执行器
     */
    private ExecutorService executor;

    /**
     * 用来控制并发数的信号量
     */
    private Semaphore semaphore;

    private String instanceId;
    private String instanceName;

    @Override
    public boolean runInThread(Runnable runnable) {
        // 先尝试拿一个许可，拿不到就让 Quartz 认为没可用线程
        if (!semaphore.tryAcquire()) {
            return false;
        }
        executor.submit(() -> {
            try {
                runnable.run();
            } finally {
                // 释放许可
                semaphore.release();
            }
        });
        return true;
    }

    @Override
    public int blockForAvailableThreads() {
        // 返回当前可用许可数
        return semaphore.availablePermits();
    }

    @Override
    public void initialize() {
        // 初始化信号量和虚拟线程执行器
        this.semaphore = new Semaphore(threadCount);
        this.executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());
    }

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void shutdown(boolean waitForJobsToComplete) {
        executor.shutdown();
        if (waitForJobsToComplete) {
            try {
                executor.awaitTermination(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public int getPoolSize() {
        return threadCount;
    }

    @Override
    public void setInstanceId(String schedInstId) {
        this.instanceId = schedInstId;
    }

    @Override
    public void setInstanceName(String schedName) {
        this.instanceName = schedName;
    }
}
