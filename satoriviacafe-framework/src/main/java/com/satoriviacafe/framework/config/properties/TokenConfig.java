package com.satoriviacafe.framework.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author shy
 * @since 2025年06月30日
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "token")
public class TokenConfig {
    /**
     * 令牌过期时间，单位为秒
     */
    private Long expireTime = 3600L;

    /**
     * 刷新令牌过期时间，单位为秒
     */
    private Long refreshExpireTime = 2592000L;

    /**
     * 是否启用刷新令牌
     */
    private Boolean enableRefresh = true;

    /**
     * JWT令牌头部名称
     */
    private String header;

    /**
     * JWT签名密钥
     */
    private String secret;
}
