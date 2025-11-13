package com.satoriviacafe.common.utils.satoriviacafe;

import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author shy
 * @since 2025年05月17日
 */
public class I18nUtil {
    /**
     * 获取当前语言代码
     * @return 语言代码
     */
    static public String getLangCode() {
        return LocaleContextHolder.getLocale().toLanguageTag();
    }

}
