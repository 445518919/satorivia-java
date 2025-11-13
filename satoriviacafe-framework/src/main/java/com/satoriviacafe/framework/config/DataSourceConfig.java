package com.satoriviacafe.framework.config;

import com.satoriviacafe.framework.datasource.DynamicDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    @Bean(name = "masterDataSource")
    @ConfigurationProperties("spring.datasource.hikari.master")
    @Primary
    public DataSource masterDataSource() {
        return new HikariDataSource();
    }

    @Bean(name = "slaveDataSource")
    @ConfigurationProperties("spring.datasource.hikari.slave")
    @ConditionalOnProperty(prefix = "spring.datasource.hikari.slave", name = "enabled", havingValue = "true")
    public DataSource slaveDataSource() {
        return new HikariDataSource();
    }

    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource(
            @Qualifier("masterDataSource") DataSource masterDataSource,
            @Autowired(required = false) @Qualifier("slaveDataSource") DataSource slaveDataSource) {

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("MASTER", masterDataSource);
        if (slaveDataSource != null) {
            targetDataSources.put("SLAVE", slaveDataSource);
        }

        return new DynamicDataSource(masterDataSource, targetDataSources);
    }
}
