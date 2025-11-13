package com.satoriviacafe.framework.config;

import lombok.RequiredArgsConstructor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * 异步任务及虚拟线程上下文传播配置：
 * - 复制 SecurityContextHolder（由 DelegatingSecurityContextExecutor 完成）
 * - 复制 RequestContextHolder
 * - 复制 LocaleContextHolder
 *
 * 使用容器中定义的虚拟线程池 bean threadPoolTaskExecutor
 */
@Configuration
@RequiredArgsConstructor
public class AsyncConfig implements AsyncConfigurer {

    /**
     * 注入 ThreadPoolConfig 中定义的虚拟线程池
     */
    @Qualifier("threadPoolTaskExecutor")
    private final ExecutorService virtualExecutor;

    @Override
    public Executor getAsyncExecutor() {
        // 先拿到上下文复制器
        final TaskDecorator decorator = contextCopyingDecorator();
        // 构造一个 Executor，把每个 Runnable 都先用 decorator 包装，然后提交给虚拟线程池
        Executor copyingExecutor = runnable -> virtualExecutor.submit(decorator.decorate(runnable));
        // 再用 Spring Security 的包装器传播 SecurityContext
        return new DelegatingSecurityContextExecutor(copyingExecutor);
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }

    private TaskDecorator contextCopyingDecorator() {
        return runnable -> {
            // 捕获父线程的 RequestAttributes 和 Locale
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            Locale locale = LocaleContextHolder.getLocale();

            return () -> {
                try {
                    // 在子线程或虚拟线程中恢复上下文
                    RequestContextHolder.setRequestAttributes(requestAttributes);
                    LocaleContextHolder.setLocale(locale);

                    runnable.run();
                } finally {
                    // 清理，防止 ThreadLocal 泄露
                    RequestContextHolder.resetRequestAttributes();
                    LocaleContextHolder.resetLocaleContext();
                }
            };
        };
    }
}
