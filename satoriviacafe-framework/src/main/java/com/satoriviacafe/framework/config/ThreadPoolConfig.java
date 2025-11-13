package com.satoriviacafe.framework.config;

import com.satoriviacafe.common.config.SatoriviacafeConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 线程池配置
 *
 * @author satoriviacafe
 **/
@Configuration
@RequiredArgsConstructor
public class ThreadPoolConfig {

    private final SatoriviacafeConfig config;

    /**
     * 无界虚拟线程池（如需限流可自行包装 Semaphore 或 TaskDecorator）
     * 在容器关闭时会调用 shutdown()
     */
    @Bean(name = "threadPoolTaskExecutor", destroyMethod = "shutdown")
    @Primary
    public ExecutorService taskExecutor() {
        if (config.isVirtual()) return Executors.newVirtualThreadPerTaskExecutor();
        else return Executors.newCachedThreadPool();
    }

    /**
     * 定时任务线程池：40 条并发虚拟线程
     * 在容器关闭时会调用 shutdown()
     */
    @Bean(name = "scheduledExecutorService", destroyMethod = "shutdown")
    protected ScheduledExecutorService scheduledExecutorService() {
        return Executors.newScheduledThreadPool(
                40,
                Thread.ofVirtual().factory()
        );
    }
}
