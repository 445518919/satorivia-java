package com.satoriviacafe.common.exception.user;

import com.satoriviacafe.common.exception.ServiceException;

import java.io.Serial;

/**
 * 用户信息异常类
 *
 * @author satoriviacafe
 */
public class UserException extends ServiceException {
    @Serial
    private static final long serialVersionUID = 1L;

    public UserException(String i18nKey, Object[] args) {
        super("user", i18nKey, args, null);
    }
}
