package com.satoriviacafe.framework.config;

import com.satoriviacafe.common.config.SatoriviacafeConfig;
import com.satoriviacafe.common.config.SatoriviacafeUndertowConfig;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.handlers.encoding.ContentEncodingRepository;
import io.undertow.server.handlers.encoding.EncodingHandler;
import io.undertow.server.handlers.encoding.GzipEncodingProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.embedded.undertow.UndertowBuilderCustomizer;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xnio.Option;
import org.xnio.Options;
import org.xnio.Xnio;
import org.xnio.XnioWorker;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;

@Configuration
@Slf4j
public class UndertowConfig {

    private final ServerProperties.Undertow undertow;
    private final SatoriviacafeUndertowConfig satoriviacafeUndertow;
    private final SatoriviacafeConfig satoriviacafe;

    public UndertowConfig(ServerProperties server, SatoriviacafeUndertowConfig undertowConfig, SatoriviacafeConfig satoriviacafe) {
        undertow = server.getUndertow();
        this.satoriviacafeUndertow = undertowConfig;
        this.satoriviacafe = satoriviacafe;
    }


    /**
     * 获取最优的IO线程数
     * 基于CPU核心数和用户配置自动计算
     */
    private int getOptimalIoThreads() {
        String configValue = satoriviacafeUndertow.getIoThreads();

        // 如果用户明确指定了数字，直接使用
        if (configValue != null && !configValue.equalsIgnoreCase("auto")) {
            try {
                return Integer.parseInt(configValue);
            } catch (NumberFormatException e) {
                log.warn("无效的IO线程数配置: {}, 将使用自动计算值", configValue);
            }
        }

        // 自动计算逻辑：获取CPU核心数
        int cpuCores = Runtime.getRuntime().availableProcessors();

        // 计算推荐的IO线程数（CPU核心数的1-2倍）
        int recommendedThreads;

        // 根据CPU核心数使用不同的乘数
        if (cpuCores <= 2) {
            // 小型机器：每个核心2个IO线程
            recommendedThreads = cpuCores * 2;
        } else if (cpuCores <= 8) {
            // 中型机器：每个核心1.5个IO线程
            recommendedThreads = Math.max(2, (int) (cpuCores * 1.5));
        } else {
            // 大型机器：每个核心1个IO线程
            recommendedThreads = cpuCores;
        }

        // 确保至少有2个IO线程
        return Math.max(2, recommendedThreads);
    }

    /**
     * Undertow 配置，使用虚拟线程池
     * 通过 XnioWorker 实现虚拟线程支持
     *
     * @param executorService 虚拟线程池
     * @return ServletWebServerFactory
     */
    @Bean
    public ServletWebServerFactory servletWebServerFactory(ExecutorService executorService) {
        UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();

        // 确定最优的IO线程数
        int optimalIoThreads = getOptimalIoThreads();

        // 使用自动计算的IO线程数或配置中的值
        int ioThreads = undertow.getThreads().getIo() != null ?
                undertow.getThreads().getIo() : optimalIoThreads;

        int workerThreads = satoriviacafe.isVirtual() ?
                (undertow.getThreads().getWorker() != null ? undertow.getThreads().getWorker() : 0) :
                (undertow.getThreads().getWorker() != null ? undertow.getThreads().getWorker() :
                        Runtime.getRuntime().availableProcessors() * 8);

        log.info("虚拟线程模式: {}, IO线程数: {} ({}), Worker线程数: {} ({})",
                satoriviacafe.isVirtual() ? "启用" : "禁用",
                ioThreads,
                undertow.getThreads().getIo() != null ? "配置指定" : "自动计算",
                workerThreads,
                satoriviacafe.isVirtual() && undertow.getThreads().getWorker() == null ? "虚拟线程默认值" : "配置指定");

        // 记录系统信息
        log.info("系统信息: CPU核心数: {}, JVM可用内存: {}MB",
                Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().maxMemory() / (1024 * 1024));

        // 配置访问日志
        configureAccessLog(factory);

        // 配置DeploymentInfo (所有Servlet相关设置)
        factory.addDeploymentInfoCustomizers(deploymentInfo -> {
            // 设置URL编码
            if (undertow.getUrlCharset() != null) {
                deploymentInfo.setUrlEncoding(undertow.getUrlCharset().name());
            }

            // 设置是否在启动时初始化过滤器
            deploymentInfo.setEagerFilterInit(undertow.isEagerFilterInit());

            // 设置转发时是否保留路径
            deploymentInfo.setPreservePathOnForward(undertow.isPreservePathOnForward());

            log.debug("已配置Undertow DeploymentInfo基本选项");
        });

        try {
            final Xnio xnio = Xnio.getInstance();
            final XnioWorker worker = xnio.createWorkerBuilder()
                    .setExternalExecutorService(executorService)
                    .setWorkerName("satoriviacafe-virtual-worker")
                    .setWorkerIoThreads(ioThreads) // 使用最优IO线程数
                    .setMaxWorkerPoolSize(workerThreads)
                    .build();

            // 添加构建器自定义器
            factory.addBuilderCustomizers(getBuilderCustomizer(worker));

            // 添加Gzip支持
            if (satoriviacafeUndertow.isEnableCompression()) {
                factory.addDeploymentInfoCustomizers(deploymentInfo ->
                        deploymentInfo.addInitialHandlerChainWrapper(getCompressionHandlerWrapper()));
                log.info("已启用Gzip压缩支持");
            }

            log.info("Undertow虚拟线程配置已完成");
        } catch (Exception e) {
            log.error("配置 Undertow 虚拟线程失败", e);
        }

        return factory;
    }

    /**
     * 创建处理Gzip压缩的HandlerWrapper
     */
    private HandlerWrapper getCompressionHandlerWrapper() {
        return handler -> {
            ContentEncodingRepository repository = new ContentEncodingRepository();
            repository.addEncodingHandler("gzip", new GzipEncodingProvider(), 50);
            return new EncodingHandler(handler, repository);
        };
    }

    /**
     * 创建Builder自定义器
     */
    private UndertowBuilderCustomizer getBuilderCustomizer(XnioWorker worker) {
        return builder -> {
            // 设置worker
            builder.setWorker(worker);

            // 设置默认启用 HTTP/2
            builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true);

            // 设置最大HTTP POST大小 (正确的方法是通过UndertowOptions.MAX_ENTITY_SIZE)
            builder.setServerOption(UndertowOptions.MAX_ENTITY_SIZE, undertow.getMaxHttpPostSize().toBytes());

            // 设置缓冲区大小和直接缓冲区选项
            if (undertow.getBufferSize() != null) {
                builder.setBufferSize((int) undertow.getBufferSize().toBytes());
            }

            if (undertow.getDirectBuffers() != null) {
                builder.setDirectBuffers(undertow.getDirectBuffers());
            }

            // 设置无请求超时
            if (undertow.getNoRequestTimeout() != null) {
                builder.setServerOption(UndertowOptions.NO_REQUEST_TIMEOUT,
                        (int) undertow.getNoRequestTimeout().toMillis());
            }

            // 设置最大头部数量（通过ServerOptions）
            builder.setServerOption(UndertowOptions.MAX_HEADERS, undertow.getMaxHeaders());

            // 设置最大参数数量
            builder.setServerOption(UndertowOptions.MAX_PARAMETERS, undertow.getMaxParameters());

            // 设置最大Cookie数量
            builder.setServerOption(UndertowOptions.MAX_COOKIES, undertow.getMaxCookies());

            // 设置是否总是添加KeepAlive头
            builder.setServerOption(UndertowOptions.ALWAYS_SET_KEEP_ALIVE, undertow.isAlwaysSetKeepAlive());

            // 设置在URL中解码斜杠
            if (undertow.getDecodeSlash() != null) {
                builder.setServerOption(UndertowOptions.DECODE_URL, undertow.getDecodeSlash());
            }

            // 应用所有自定义选项
            applyOptions(builder, undertow.getOptions());

            // 确保HTTP2启用（如果配置文件没有明确禁用）
            if (!isOptionExplicitlyDisabled(undertow.getOptions().getServer(), UndertowOptions.ENABLE_HTTP2.getName())) {
                log.debug("已启用HTTP/2支持");
            }
        };
    }

    /**
     * 配置访问日志
     */
    private void configureAccessLog(UndertowServletWebServerFactory factory) {
        ServerProperties.Undertow.Accesslog accesslog = undertow.getAccesslog();
        if (accesslog.isEnabled()) {
            factory.setAccessLogEnabled(true);
            factory.setAccessLogDirectory(accesslog.getDir());
            factory.setAccessLogPattern(accesslog.getPattern());
            factory.setAccessLogPrefix(accesslog.getPrefix());
            factory.setAccessLogSuffix(accesslog.getSuffix());
            factory.setAccessLogRotate(accesslog.isRotate());

            log.info("已启用Undertow访问日志, 目录: {}, 模式: {}",
                    accesslog.getDir().getAbsolutePath(), accesslog.getPattern());
        }
    }

    /**
     * 应用所有配置的Undertow选项
     */
    private void applyOptions(Undertow.Builder builder, ServerProperties.Undertow.Options options) {
        // 应用服务器选项
        if (options.getServer() != null) {
            options.getServer().forEach((key, value) -> {
                try {
                    // 通过反射获取UndertowOptions中的常量
                    Field field = UndertowOptions.class.getField(key);
                    Option<?> option = (Option<?>) field.get(null);
                    setOption(builder::setServerOption, option, value);
                    log.debug("已设置服务器选项: {} = {}", key, value);
                } catch (Exception e) {
                    log.warn("设置服务器选项失败: {} = {}, 错误: {}", key, value, e.getMessage());
                }
            });
        }

        // 应用套接字选项
        if (options.getSocket() != null) {
            options.getSocket().forEach((key, value) -> {
                try {
                    Field field = Options.class.getField(key);
                    Option<?> option = (Option<?>) field.get(null);
                    setOption(builder::setSocketOption, option, value);
                    log.debug("已设置套接字选项: {} = {}", key, value);
                } catch (Exception e) {
                    log.warn("设置套接字选项失败: {} = {}, 错误: {}", key, value, e.getMessage());
                }
            });
        }
    }

    /**
     * 设置选项值，使用选项自身的parseValue方法处理类型转换
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private <T> void setOption(BiConsumer<Option<T>, T> setter, Option option, String value) {
        try {
            // 使用选项自己的parseValue方法解析值
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Object parsedValue = option.parseValue(value, classLoader);
            setter.accept(option, (T) parsedValue);
        } catch (Exception e) {
            log.warn("解析选项值失败: {} = {}, 错误: {}", option.getName(), value, e.getMessage());

            // 尝试简单类型转换
            try {
                // 基于常见类型进行简单转换
                if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                    setter.accept(option, (T) Boolean.valueOf(value));
                } else if (value.matches("-?\\d+")) {
                    try {
                        long longVal = Long.parseLong(value);
                        if (longVal <= Integer.MAX_VALUE && longVal >= Integer.MIN_VALUE) {
                            setter.accept(option, (T) Integer.valueOf((int) longVal));
                        } else {
                            setter.accept(option, (T) Long.valueOf(longVal));
                        }
                    } catch (Exception in) {
                        setter.accept(option, (T) Integer.valueOf(value));
                    }
                } else {
                    // 作为字符串处理
                    setter.accept(option, (T) value);
                }
            } catch (Exception ex) {
                log.error("设置选项值最终失败: {} = {}", option.getName(), value);
            }
        }
    }

    /**
     * 检查配置是否明确禁用了某个选项
     */
    private boolean isOptionExplicitlyDisabled(Map<String, String> options, String key) {
        if (options != null && options.containsKey(key)) {
            String value = options.get(key);
            return "false".equalsIgnoreCase(value) || "0".equals(value) || "no".equalsIgnoreCase(value);
        }
        return false;
    }
}
