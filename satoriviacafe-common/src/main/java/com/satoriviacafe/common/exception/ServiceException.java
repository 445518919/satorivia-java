package com.satoriviacafe.common.exception;

import com.satoriviacafe.common.constant.HttpStatus;
import lombok.Getter;

import java.io.Serial;

/**
 * 业务异常
 *
 * @author satoriviacafe
 */
@Getter
public class ServiceException extends GlobalException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 业务错误码（可用于前端区分不同业务错误）
     */
    private final Integer code;

    public ServiceException(String module, String i18nKey, Object[] args, String defaultMessage, String detailMessage, Integer code) {
        super(module, i18nKey, args, defaultMessage, detailMessage);
        this.code = code;
    }

    public ServiceException(String module, String i18nKey, Object[] args, String defaultMessage, Integer code) {
        super(module, i18nKey, args, defaultMessage);
        this.code = code;
    }

    public ServiceException(String module, String i18nKey, Object[] args, Integer code) {
        super(module, i18nKey, args);
        this.code = code;
    }

    public ServiceException(String module, String defaultMessage, Integer code) {
        super(module, defaultMessage);
        this.code = code;
    }

    public ServiceException(String defaultMessage, Integer code) {
        super(defaultMessage);
        this.code = code;
    }

    // 可选：仅传业务错误码，默认消息
    public ServiceException(Integer code) {
        super("Service Error");
        this.code = code;
    }

    public ServiceException(String defaultMessage) {
        super(defaultMessage);
        this.code = HttpStatus.SERVICE_ERROR;
    }
}
