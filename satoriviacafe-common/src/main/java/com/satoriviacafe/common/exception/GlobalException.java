package com.satoriviacafe.common.exception;

import com.satoriviacafe.common.exception.base.BaseException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;

/**
 * 全局异常
 *
 * @author satoriviacafe
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GlobalException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 错误明细，内部调试错误
     * <p>
     * 和 {@link CommonResult#getDetailMessage()} 一致的设计
     */
    private String detailMessage;

    public GlobalException(String module, String i18nKey, Object[] args, String defaultMessage, String detailMessage) {
        super(module, i18nKey, args, defaultMessage);
        this.detailMessage = detailMessage;
    }

    public GlobalException(String module, String i18nKey, Object[] args, String defaultMessage) {
        super(module, i18nKey, args, defaultMessage);
    }

    public GlobalException(String module, String i18nKey, Object[] args) {
        super(module, i18nKey, args);
    }

    public GlobalException(String module, String defaultMessage) {
        super(module, defaultMessage);
    }

    public GlobalException(String defaultMessage) {
        super(defaultMessage);
    }
}
