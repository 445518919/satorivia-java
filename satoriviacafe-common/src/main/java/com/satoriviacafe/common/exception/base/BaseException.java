package com.satoriviacafe.common.exception.base;

import com.satoriviacafe.common.utils.MessageUtils;
import com.satoriviacafe.common.utils.VStringUtils;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 基础异常
 *
 * @author satoriviacafe
 */
@Getter
public class BaseException extends RuntimeException implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 所属模块
     */
    private final String module;

    /**
     * 错误码
     */
    private final String i18nKey;

    /**
     * 错误码对应的参数
     */
    private final Object[] args;

    /**
     * 错误消息
     */
    private final String defaultMessage;

    public BaseException(String module, String i18nKey, Object[] args, String defaultMessage) {
        this.module = module;
        this.i18nKey = i18nKey;
        this.args = args;
        this.defaultMessage = defaultMessage;
    }

    public BaseException(String module, String i18nKey, Object[] args) {
        this(module, i18nKey, args, null);
    }

    public BaseException(String module, String defaultMessage) {
        this(module, null, null, defaultMessage);
    }

    public BaseException(String i18nKey, Object[] args) {
        this(null, i18nKey, args, null);
    }

    public BaseException(String defaultMessage) {
        this(null, null, null, defaultMessage);
    }

    public BaseException() {
        this("An error occurred");
    }

    @Override
    public String getMessage() {
        String message = null;
        if (!VStringUtils.isEmpty(i18nKey)) {
            message = MessageUtils.message(i18nKey, args);
        }
        if (message == null) {
            message = defaultMessage;
        }
        return message;
    }

}
