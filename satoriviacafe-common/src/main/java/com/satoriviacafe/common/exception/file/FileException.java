package com.satoriviacafe.common.exception.file;

import com.satoriviacafe.common.exception.ServiceException;

import java.io.Serial;


/**
 * 文件信息异常类
 *
 * @author satoriviacafe
 */
public class FileException extends ServiceException {
    @Serial
    private static final long serialVersionUID = 1L;

    public FileException(String i18nKey, Object[] args) {
        super("file", i18nKey, args, null);
    }

}
