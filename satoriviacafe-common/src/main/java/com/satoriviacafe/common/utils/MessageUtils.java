package com.satoriviacafe.common.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import com.satoriviacafe.common.utils.spring.SpringUtils;

/**
 * 获取i18n资源文件
 *
 * @author satoriviacafe
 */
public class MessageUtils {

    /**
     * 根据消息键和参数 获取消息 委托给spring messageSource
     *
     * @param code 消息键
     * @param args 参数
     * @return 获取国际化翻译值
     */
    public static String message(String code, Object... args) {
        MessageSource messageSource = SpringUtils.getBean(MessageSource.class);
        try {
            return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
        }catch (Exception ignore){
            return code; // 如果获取失败，返回原始的消息键
        }
    }

}
