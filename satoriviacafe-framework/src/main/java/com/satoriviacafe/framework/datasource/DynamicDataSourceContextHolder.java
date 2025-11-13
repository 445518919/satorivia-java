package com.satoriviacafe.framework.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("preview")
public class DynamicDataSourceContextHolder {

    public static final Logger log = LoggerFactory.getLogger(DynamicDataSourceContextHolder.class);

    // 替换为 ScopedValue
    private static final ScopedValue<String> CONTEXT_HOLDER = ScopedValue.newInstance();

    /**
     * 设置数据源变量（ScopedValue只能在作用域内设置）
     */
    public static void runWithDataSourceType(String dsType, Runnable runnable) {
        log.info("切换到{}数据源", dsType);
        ScopedValue.where(CONTEXT_HOLDER, dsType).run(runnable);
    }

    /**
     * 获取数据源变量
     */
    public static String getDataSourceType() {
        return CONTEXT_HOLDER.get();
    }
}
