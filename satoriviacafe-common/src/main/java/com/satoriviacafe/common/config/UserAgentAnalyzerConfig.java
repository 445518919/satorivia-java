package com.satoriviacafe.common.config;

import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * UserAgentAnalyzer配置
 *
 * @author shy
 * @since 2026年04月29日
 */
@Configuration
public class UserAgentAnalyzerConfig {
    /**
     * UserAgentAnalyzer Bean
     */
    @Bean
    public UserAgentAnalyzer userAgentAnalyzer() {
        return UserAgentAnalyzer.newBuilder().hideMatcherLoadStats().withCache(1000).build();
    }
}
