package com.satoriviacafe.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author shy
 * @since 2025年06月30日
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "satoriviacafe.undertow")
public class SatoriviacafeUndertowConfig {
    /**
     * IO线程数设置
     * - 数字：直接指定线程数
     * - auto：自动根据CPU核心数设置（默认值）
     */
    private String ioThreads = "auto";

    /**
     * 默认在虚拟线程模式下启用HTTP/2
     */
    private boolean enableHttp2 = true;

    /**
     * 是否自动启用压缩
     */
    private boolean enableCompression = true;
}
